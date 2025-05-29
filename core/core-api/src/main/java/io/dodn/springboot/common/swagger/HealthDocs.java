package io.dodn.springboot.common.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface HealthDocs {

    /**
     * 애플리케이션의 동작 상태를 확인하는 헬스체크 엔드포인트입니다.
     *
     * @return 애플리케이션이 정상적으로 동작하면 200 OK, 문제가 있을 경우 503 Service Unavailable 상태의 응답을 반환합니다.
     */
    @Operation(summary = "애플리케이션 상태 확인", description = "애플리케이션의 동작 상태를 확인하는 헬스체크 엔드포인트입니다.", tags = { "Health Check" })
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "503", description = "통신 이상") })
    public ResponseEntity<Void> health();

}
