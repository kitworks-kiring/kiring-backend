package io.dodn.springboot.common.swagger;

import io.dodn.springboot.member.controller.response.MemberResponse;
import io.dodn.springboot.member.controller.response.MembersResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface MemberDocs {

    @Operation(summary = "회원 정보 조회", description = "특정 회원의 정보를 조회하는 엔드포인트입니다.", tags = { "Member Management" })
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "회원이 존재하지 않음") })
    io.dodn.springboot.common.support.response.ApiResponse<MemberResponse> getMemberById(@PathVariable Long memberId);

    @Operation(summary = "팀과 함께 회원 목록 조회", description = "현재 등록된 회원의 목록을 팀 정보와 함께 조회하는 엔드포인트입니다.",
            tags = { "Member Management" })
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "회원이 존재하지 않음") })
    io.dodn.springboot.common.support.response.ApiResponse<MembersResponse> getMembersWithTeam();

    @Operation(summary = "팀별 회원 목록 조회", description = "특정 팀에 속한 회원들의 목록을 조회하는 엔드포인트입니다.",
            tags = { "Member Management" })
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "팀이 존재하지 않음") })
    io.dodn.springboot.common.support.response.ApiResponse<MembersResponse> getMemberByTeam(
            @RequestParam("teamId") final Long teamId);

    @Operation(summary = "회원 수 조회", description = "현재 등록된 회원의 수를 조회하는 엔드포인트입니다.", tags = { "Member Management" })
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "회원이 존재하지 않음") })
    io.dodn.springboot.common.support.response.ApiResponse<Long> getMemberCount();

    @Operation(summary = "회원 정보 수정", description = "특정 회원의 정보를 수정하는 엔드포인트입니다.", tags = { "Member Management" })
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "회원이 존재하지 않음") })
    io.dodn.springboot.common.support.response.ApiResponse<MemberResponse> updateMember();
}
