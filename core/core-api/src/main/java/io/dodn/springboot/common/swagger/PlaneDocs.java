package io.dodn.springboot.common.swagger;

import io.dodn.springboot.common.annotation.LoginUser;
import io.dodn.springboot.plane.controller.request.SendMessageRequest;
import io.dodn.springboot.plane.controller.response.MessageResponse;
import io.dodn.springboot.plane.controller.response.SendMessageResponse;
import io.dodn.springboot.plane.controller.response.TodayMessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface PlaneDocs {
    @Operation(summary = "쪽지 발송", description = "특정 회원에게 쪽지를 발송하는 엔드포인트입니다.", tags = { "Plane Management" })
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "회원이 존재하지 않음") })
    io.dodn.springboot.common.support.response.ApiResponse<SendMessageResponse> sendMessage(
            @RequestBody SendMessageRequest request
    );

    @Operation(summary = "쪽지 조회", description = "특정 회원이 쪽지를 조회하는 엔드포인트입니다.", tags = { "Plane Management" })
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공")})
    public io.dodn.springboot.common.support.response.ApiResponse<List<MessageResponse>> readMessage(
            @Parameter(hidden = true) @LoginUser long readerId
    );

    @Operation(summary = "쪽지 존재 여부", description = "오늘 받은 편지 존재 여부 API (팝업 알림용).", tags = { "Plane Management" })
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공")})
    public io.dodn.springboot.common.support.response.ApiResponse<TodayMessageResponse> getTodayMessage(
            @Parameter(hidden = true) @LoginUser long readerId
    );
}
