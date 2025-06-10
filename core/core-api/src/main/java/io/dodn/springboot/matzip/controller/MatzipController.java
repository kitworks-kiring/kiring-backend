package io.dodn.springboot.matzip.controller;

import io.dodn.springboot.common.annotation.LoginUser;
import io.dodn.springboot.common.support.response.ApiResponse;
import io.dodn.springboot.matzip.controller.response.PlaceResponse;
import io.dodn.springboot.matzip.domain.MatzipService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MatzipController {
    private final MatzipService matzipService;

    public MatzipController(final MatzipService matzipService) {
        this.matzipService = matzipService;
    }

    @GetMapping("/places")
    public ApiResponse<Page<PlaceResponse>> getAllPlaces(
            // 로그인한 사용자 ID를 받습니다. 비로그인 시 null이 전달되도록 ArgumentResolver 설정 필요
            @Parameter(hidden = true) @LoginUser final Long memberId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<PlaceResponse> placeResponses = matzipService.findAllPlaces(memberId, pageable);
        return ApiResponse.success(placeResponses);
    }
}
