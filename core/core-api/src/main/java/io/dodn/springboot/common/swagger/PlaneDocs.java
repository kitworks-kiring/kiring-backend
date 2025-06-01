package io.dodn.springboot.common.swagger;

import io.dodn.springboot.plane.controller.request.SendMessageRequest;
import io.dodn.springboot.plane.controller.response.SendMessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;

public interface PlaneDocs {
    @Operation(summary = "쪽지 발송", description = "특정 회원에게 쪽지를 발송하는 엔드포인트입니다.", tags = { "Plane Management" })
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "회원이 존재하지 않음") })
    io.dodn.springboot.common.support.response.ApiResponse<SendMessageResponse> sendMessage(
            @RequestBody SendMessageRequest request
    );

    @Operation(summary = "쪽지 읽음 처리", description = "특정 회원이 쪽지를 읽음 처리하는 엔드포인트입니다.", tags = { "Plane Management" })
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "쪽지가 존재하지 않음") })
    io.dodn.springboot.common.support.response.ApiResponse<?> readMessage(
            @RequestBody io.dodn.springboot.plane.controller.request.ReadMessageRequest request
    );


    }
