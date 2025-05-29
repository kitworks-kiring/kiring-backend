package io.dodn.springboot.member.controller;

import io.dodn.springboot.common.support.response.ApiResponse;
import io.dodn.springboot.common.swagger.MemberDocs;
import io.dodn.springboot.member.controller.response.MemberResponse;
import io.dodn.springboot.member.controller.response.MembersResponse;
import io.dodn.springboot.member.domain.MemberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController implements MemberDocs {

    private final MemberService memberService;

    /**
     * MemberController의 인스턴스를 생성하고 MemberService를 주입합니다.
     *
     * @param memberService 회원 관련 비즈니스 로직을 처리하는 서비스
     */
    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * 주어진 ID에 해당하는 회원 정보를 조회하여 반환합니다.
     *
     * @param memberId 조회할 회원의 고유 ID
     * @return 회원 정보를 담은 ApiResponse 객체
     */
    @GetMapping("/members/{memberId}")
    public ApiResponse<MemberResponse> getMemberById(@PathVariable("memberId") final Long memberId) {
        return ApiResponse.success(MemberResponse.of(memberService.findMemberById(memberId)));
    }

    /**
     * 모든 회원과 그에 속한 팀 정보를 조회하여 반환합니다.
     *
     * @return 회원과 팀 정보를 포함하는 MembersResponse 객체를 성공 응답으로 래핑한 결과
     */
    @GetMapping("/members")
    public ApiResponse<MembersResponse> getMembersWithTeam() {
        return ApiResponse.success(MembersResponse.of(memberService.findAllMembersWithTeamUsingFetchJoin()));
    }

    /**
     * 전체 회원 수를 조회하여 반환합니다.
     *
     * @return 전체 회원 수를 포함한 ApiResponse 객체
     */
    @GetMapping("/members/count")
    public ApiResponse<Long> getMemberCount() {
        return ApiResponse.success(memberService.countMembers());
    }

    /**
     * 지정된 팀 ID에 속한 모든 회원 목록을 조회하여 반환합니다.
     *
     * @param teamId 조회할 팀의 ID
     * @return 팀에 소속된 회원 목록을 포함하는 ApiResponse
     */
    @GetMapping("/teams/members")
    public ApiResponse<MembersResponse> getMemberByTeam(@RequestParam("teamId") final Long teamId) {
        return ApiResponse.success(MembersResponse.of(memberService.findAllMemberByTeamId(teamId)));
    }


}
