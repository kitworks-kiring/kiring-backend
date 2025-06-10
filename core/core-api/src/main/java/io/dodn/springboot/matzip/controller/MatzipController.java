package io.dodn.springboot.matzip.controller;

import io.dodn.springboot.common.annotation.LoginUser;
import io.dodn.springboot.common.dto.CustomPageResponse;
import io.dodn.springboot.common.support.response.ApiResponse;
import io.dodn.springboot.matzip.controller.response.LikeToggleResponse;
import io.dodn.springboot.matzip.controller.response.PlaceResponse;
import io.dodn.springboot.matzip.domain.MatzipService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MatzipController {
    private final MatzipService matzipService;

    public MatzipController(final MatzipService matzipService) {
        this.matzipService = matzipService;
    }

    @GetMapping("/places")
    public ApiResponse<CustomPageResponse<PlaceResponse>> getAllPlaces(
            @Parameter(hidden = true) @LoginUser final Long memberId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<PlaceResponse> placeResponses = matzipService.findAllPlaces(memberId, pageable);

        CustomPageResponse<PlaceResponse> response = new CustomPageResponse<>(placeResponses);
        return ApiResponse.success(response);
    }

    @PostMapping("/toggle/like/{placeId}")
    public ApiResponse<LikeToggleResponse> toggleLikePlace(
            @Parameter(hidden = true) @LoginUser final Long memberId,
            @PathVariable("placeId") final Long placeId
    ) {
        final LikeToggleResponse likeToggleResponse = matzipService.toggleLike(memberId, placeId);
        return ApiResponse.success(likeToggleResponse);
    }

}
