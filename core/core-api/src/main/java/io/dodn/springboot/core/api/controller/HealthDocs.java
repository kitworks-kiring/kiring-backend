package io.dodn.springboot.core.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface HealthDocs {
    @Operation(summary = "Health Check API", description = "서버 통신 여부 기능", tags = {"Health"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"
//                    ,content = @Content(mediaType = "application/json"
//                    ,schema = @Schema(implementation = Void.class))
            ),
            @ApiResponse(responseCode = "404", description = "통신 불가능")
    })
    public ResponseEntity<Void> health();
}
