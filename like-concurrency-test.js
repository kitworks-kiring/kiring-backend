import ohttp from 'k6/http';
import { check, sleep, group } from 'k6';
import { Trend } from 'k6/metrics';

const BASE_URL = 'https://api.kiring.shop/api/v1';
// 💡 각기 다른 사용자인 것처럼 보이게 하려면, 실제로는 여러 개의 테스트용 토큰을 준비하고 랜덤하게 사용해야 합니다.
// 여기서는 편의상 하나의 토큰을 사용합니다.
const USER_ACCESS_TOKEN = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI4IiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTc1MzUxNDYyNX0.hZqIq0WHBWVU9e03QeQd27ApQzn4v2R9XXHnYZEcqKk';
const TARGET_PLACE_ID = 22; // 🎯 하나의 특정 맛집에 부하 집중

const headers = { 'Authorization': `Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI4IiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTc1MzUxNDYyNX0.hZqIq0WHBWVU9e03QeQd27ApQzn4v2R9XXHnYZEcqKk` };
const toggleLikeApiTrend = new Trend('toggle_like_api_duration');

export const options = {
    scenarios: {
        concurrency_test: {
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                { duration: '20s', target: 100 }, // 20초간 동시 사용자 100명까지 증가
                { duration: '30s', target: 100 }, // 100명으로 30초간 유지
            ],
        },
    },
};

export default function () {
    const res = http.post(`${BASE_URL}/matzip/toggle/like/${TARGET_PLACE_ID}`, null, { headers });

    toggleLikeApiTrend.add(res.timings.duration);
    check(res, { 'status was 200': (r) => r.status === 200 });
    sleep(0.5); // 약간의 간격을 둠
}