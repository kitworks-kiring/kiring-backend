import http from 'k6/http';
import { check, sleep, group } from 'k6';
import { Trend } from 'k6/metrics';

// =================================================================================
// 테스트 환경 설정
// =================================================================================
//const BASE_URL = 'http://localhost:8080/api/v1'; // 테스트할 서버 주소 (로컬)
const BASE_URL = 'https://api.kiring.shop/api/v1'; // 테스트할 서버 주소 (개발계)
const USER_ACCESS_TOKEN = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI4IiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTc1MDgzODUzNH0.rsgkizqkV1tD9DWpvkLalsUNStAqqNJmjGhEEmRMcZs'; // 테스트를 위한 인증 토큰

const headers = {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${USER_ACCESS_TOKEN}`,
};

// =================================================================================
// 사용자 정의 성능 지표 생성
// =================================================================================
// 각 API별 응답 시간을 추적하기 위한 Trend 메트릭
const placesApiTrend = new Trend('places_api_duration');
const toggleLikeApiTrend = new Trend('toggle_like_api_duration');

// =================================================================================
// 테스트 시나리오 설정 (Options)
// https://k6.io/docs/using-k6/k6-options-reference/
// =================================================================================
export const options = {
    // 시나리오 1: Smoke Test (기능이 정상 동작하는지 최소한의 부하로 확인)
    // scenarios: {
    //   smoke_test: {
    //     executor: 'constant-vus',
    //     vus: 1,
    //     duration: '30s',
    //   },
    // },

    // 시나리오 2: Load Test (점진적으로 부하를 높여 시스템의 한계점 확인)
    scenarios: {
        load_test: {
            executor: 'ramping-vus', // VUser(가상 사용자) 수를 점진적으로 늘리는 실행기
            startVUs: 0,
            stages: [
                { duration: '10s', target: 20 },  // 30초 동안 사용자를 20명까지 늘림
                { duration: '20s', target: 20 },   // 20명으로 1분간 유지
                { duration: '10s', target: 0 },   // 30초 동안 사용자 0명으로 줄임
            ],
            gracefulRampDown: '30s',
        },
    },
    // 실패 기준 설정 (Thresholds)
    thresholds: {
        'http_req_failed': ['rate<0.01'],      // HTTP 요청 실패율이 1% 미만이어야 함
        'http_req_duration': ['p(95)<500'],    // 전체 요청의 95%가 500ms 안에 응답해야 함
        'toggle_like_api_duration{scenario:load_test}': ['p(95)<300'], // 좋아요 토글 API의 95%는 300ms 안에 응답
        'places_api_duration{scenario:load_test}': ['p(95)<500'], // 맛집 조회 API의 95%는 500ms 안에 응답
    },
};

// =================================================================================
// 테스트 로직 (가상 사용자가 수행할 작업)
// =================================================================================
export default function () {
    // group을 사용하면 테스트 결과를 그룹별로 묶어서 볼 수 있습니다.
    group('Matzip API Load Test', function () {

        // --- 시나리오 1: 맛집 목록 조회 (인증 불필요) ---
        group('Get Places List', function () {
            const randomPage = Math.floor(Math.random() * 5); // 0~4 페이지 랜덤 조회
            const res = http.get(`${BASE_URL}/matzip/places?page=${randomPage}&size=10`);

            // 응답 시간 커스텀 메트릭에 추가
            placesApiTrend.add(res.timings.duration);

            // 응답 검증
            check(res, {
                'is status 200': (r) => r.status === 200,
                'response body contains places': (r) => r.json('data.content') !== undefined,
            });
        });

        sleep(1); // 다음 요청 전에 1초 대기

        // --- 시나리오 2: 맛집 좋아요 토글 (인증 필요) ---
        group('Toggle Like Place', function () {
            // 실제 테스트에서는 DB에 있는 맛집 ID 목록에서 랜덤하게 선택해야 합니다.
            const randomPlaceId = Math.floor(Math.random() * 100) + 1;

            const res = http.post(`${BASE_URL}/matzip/toggle/like/${randomPlaceId}`, null, { headers });

            toggleLikeApiTrend.add(res.timings.duration);

            check(res, {
                'is status 200': (r) => r.status === 200,
                'response body contains isLiked': (r) => r.json('data.isLiked') !== undefined,
            });
        });

    });

    sleep(1); // 시나리오 반복 전 1초 대기
}