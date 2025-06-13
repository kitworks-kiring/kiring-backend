package io.dodn.springboot.common.swagger;

import io.dodn.springboot.common.annotation.LoginUser;
import io.dodn.springboot.common.support.response.ApiResponse;
import io.dodn.springboot.member.controller.request.UpdateMemberRequest;
import io.dodn.springboot.member.controller.response.MemberResponse;
import io.dodn.springboot.member.controller.response.MembersResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface MemberDocs {

    @Operation(summary = "내 정보 조회", description = "현재 로그인된 사용자의 정보를 조회합니다.", tags = { "Member Management" })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = MemberResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원이 존재하지 않음")
    })
    ApiResponse<MemberResponse> getMemberById(
            @Parameter(hidden = true) @LoginUser Long memberId
    );

    @Operation(summary = "모든 회원 및 팀 정보 조회 (Fetch Join)", description = "모든 회원의 목록을 팀 정보와 함께 Fetch Join하여 조회합니다.", tags = { "Member Management" })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = MembersResponse.class)))
    })
    ApiResponse<MembersResponse> getMembersWithTeam();

    @Operation(summary = "전체 회원 수 조회", description = "등록된 모든 회원의 수를 조회합니다.", tags = { "Member Management" })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = Long.class)))
    })
    ApiResponse<Long> getMemberCount();

    @Operation(summary = "특정 팀 소속 회원 목록 조회", description = "지정된 팀 ID에 속한 모든 회원의 목록을 조회합니다.", tags = { "Member Management" })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = MembersResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "팀 또는 해당 팀의 회원이 존재하지 않음")
    })
    ApiResponse<MembersResponse> getMemberByTeam(
            @Parameter(name = "teamId", description = "조회할 팀의 ID", required = true, in = ParameterIn.QUERY, example = "1") @RequestParam("teamId") final Long teamId
    );

    @Operation(summary = "내 정보 수정", description = "현재 로그인된 사용자의 정보를 수정합니다.", tags = { "Member Management" })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = MemberResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원이 존재하지 않음")
    })
    ApiResponse<MemberResponse> updateMember(
            @Parameter(hidden = true) @LoginUser final Long memberId,
            @RequestBody UpdateMemberRequest updateMemberRequest
    );

    @Operation(summary = "관리자 권한 테스트", description = "관리자(ADMIN) 역할이 있는 사용자만 접근 가능한 테스트 엔드포인트입니다.", tags = { "Member Management" })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "접근 권한 없음")
    })
    ApiResponse<String> getTest();
}
