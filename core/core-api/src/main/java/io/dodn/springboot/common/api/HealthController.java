package io.dodn.springboot.common.api;

import io.dodn.springboot.common.swagger.HealthDocs;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController implements HealthDocs {

    /****
     * 서비스의 상태를 확인하는 헬스 체크 엔드포인트입니다.
     *
     * 클라이언트가 `/health` 경로로 GET 요청을 보내면 HTTP 200 OK 상태로 응답합니다. 응답 본문은 없습니다.
     *
     * @return HTTP 200 OK 상태의 빈 응답
     */
    @GetMapping("/health")
    public ResponseEntity<Void> health() {

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
