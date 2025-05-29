package io.dodn.springboot.common.swagger;

import io.dodn.springboot.member.controller.response.MemberResponse;
import io.dodn.springboot.member.controller.response.MembersResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface MemberDocs {

    /**
     * 주어진 회원 ID로 특정 회원의 정보를 조회합니다.
     *
     * @param memberId 조회할 회원의 고유 식별자
     * @return 회원 정보가 포함된 API 응답 객체
     */
    @Operation(summary = "회원 정보 조회", description = "특정 회원의 정보를 조회하는 엔드포인트입니다.", tags = { "Member Management" })
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "회원이 존재하지 않음") })
    io.dodn.springboot.common.support.response.ApiResponse<MemberResponse> getMemberById(@PathVariable Long memberId);

    /**
     * 모든 등록된 회원과 각 회원의 팀 정보를 함께 조회합니다.
     *
     * @return 팀 정보가 포함된 회원 목록의 API 응답
     */
    @Operation(summary = "팀과 함께 회원 목록 조회", description = "현재 등록된 회원의 목록을 팀 정보와 함께 조회하는 엔드포인트입니다.",
            tags = { "Member Management" })
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "회원이 존재하지 않음") })
    io.dodn.springboot.common.support.response.ApiResponse<MembersResponse> getMembersWithTeam();

    /**
             * 특정 팀에 속한 회원들의 목록을 조회합니다.
             *
             * @param teamId 조회할 팀의 고유 식별자
             * @return 해당 팀에 소속된 회원 목록이 포함된 API 응답 객체
             */
            @Operation(summary = "팀별 회원 목록 조회", description = "특정 팀에 속한 회원들의 목록을 조회하는 엔드포인트입니다.",
            tags = { "Member Management" })
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "팀이 존재하지 않음") })
    public io.dodn.springboot.common.support.response.ApiResponse<MembersResponse> getMemberByTeam(
            @RequestParam("teamId") final Long teamId);

    /**
     * 현재 등록된 회원의 총 수를 반환합니다.
     *
     * @return 등록된 회원 수를 포함하는 API 응답 객체
     */
    @Operation(summary = "회원 수 조회", description = "현재 등록된 회원의 수를 조회하는 엔드포인트입니다.", tags = { "Member Management" })
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "회원이 존재하지 않음") })
    io.dodn.springboot.common.support.response.ApiResponse<Long> getMemberCount();

}
