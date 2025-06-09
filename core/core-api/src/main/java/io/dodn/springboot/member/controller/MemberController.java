package io.dodn.springboot.member.controller;

import io.dodn.springboot.common.annotation.LoginUser;
import io.dodn.springboot.common.support.response.ApiResponse;
import io.dodn.springboot.common.swagger.MemberDocs;
import io.dodn.springboot.member.controller.request.UpdateMemberRequest;
import io.dodn.springboot.member.controller.response.MemberResponse;
import io.dodn.springboot.member.controller.response.MembersResponse;
import io.dodn.springboot.member.domain.MemberService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController implements MemberDocs {
    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MemberController.class);

    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members/me")
    public ApiResponse<MemberResponse> getMemberById(
            @Parameter(hidden = true) @LoginUser final Long memberId
    ) {
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
    public ApiResponse<MembersResponse> getMemberByTeam(
            @RequestParam("teamId") final Long teamId
    ) {
        return ApiResponse.success(MembersResponse.of(memberService.findAllMemberByTeamId(teamId)));
    }

    @PutMapping("/members/me")
    public ApiResponse<MemberResponse> updateMember(
            @Parameter(hidden = true) @LoginUser final Long memberId,
            @RequestBody UpdateMemberRequest updateMemberRequest
    ) {
        return ApiResponse.success(MemberResponse.of(memberService.updateMember(memberId, updateMemberRequest.toEntity())));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/member/role")
    public ApiResponse<String> getTest() {
        log.info("apia 호출됨??");
        return ApiResponse.success("Test success");
    }


}
