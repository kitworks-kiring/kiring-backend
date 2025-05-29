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

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members/{memberId}")
    public ApiResponse<MemberResponse> getMemberById(@PathVariable("memberId") final Long memberId) {
        return ApiResponse.success(MemberResponse.of(memberService.findMemberById(memberId)));
    }

    @GetMapping("/members")
    public ApiResponse<MembersResponse> getMembersWithTeam() {
        return ApiResponse.success(MembersResponse.of(memberService.findAllMembersWithTeamUsingFetchJoin()));
    }

    @GetMapping("/members/count")
    public ApiResponse<Long> getMemberCount() {
        return ApiResponse.success(memberService.countMembers());
    }

    @GetMapping("/teams/members")
    public ApiResponse<MembersResponse> getMemberByTeam(@RequestParam("teamId") final Long teamId) {
        return ApiResponse.success(MembersResponse.of(memberService.findAllMemberByTeamId(teamId)));
    }


}
