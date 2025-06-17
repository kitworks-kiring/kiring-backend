package io.dodn.springboot.common.swagger;

import io.dodn.springboot.common.annotation.LoginUser;
import io.dodn.springboot.common.dto.CustomPageResponse;
import io.dodn.springboot.common.support.response.ApiResponse;
import io.dodn.springboot.matzip.controller.response.LikeToggleResponse;
import io.dodn.springboot.matzip.controller.response.NearbyPlaceResponse;
import io.dodn.springboot.matzip.controller.response.PlaceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface MatzipDocs {
    @Operation(summary = "모든 맛집 목록 조회", description = "등록된 모든 맛집의 목록을 페이지네이션하여 조회합니다. 로그인 시 좋아요 여부도 함께 반환합니다.", tags = { "Matzip Management" })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = CustomPageResponse.class))) // PlaceResponse를 감싸는 CustomPageResponse
    })
    ApiResponse<CustomPageResponse<PlaceResponse>> getAllPlaces(
            @Parameter(hidden = true) @LoginUser final Long memberId,
            @Parameter(hidden = true) Pageable pageable
    );

    @Operation(summary = "맛집 좋아요 토글", description = "특정 맛집에 대한 사용자의 좋아요 상태를 토글합니다. (좋아요/좋아요 취소)", tags = { "Matzip Management" })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = LikeToggleResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "맛집 또는 회원이 존재하지 않음")
    })
    ApiResponse<LikeToggleResponse> toggleLikePlace(
            @Parameter(hidden = true) @LoginUser final Long memberId,
            @Parameter(name = "placeId", description = "좋아요를 토글할 맛집의 ID", required = true, in = ParameterIn.PATH) @PathVariable("placeId") final Long placeId
    );

    @Operation(summary = "주변 맛집 검색", description = "주어진 위도, 경도, 반경 내의 맛집들을 검색하여 페이지네이션 결과로 반환합니다.", tags = { "Matzip Management" })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = Page.class))) // NearbyPlaceResponse를 감싸는 Page
    })
    public ApiResponse<CustomPageResponse<NearbyPlaceResponse>> findNearbyPlaces(
            @Parameter(name = "lat", description = "현재 위치의 위도", required = true, example = "37.53313") @RequestParam("lat") double latitude,
            @Parameter(name = "lon", description = "현재 위치의 경도", required = true, example = "126.904091") @RequestParam("lon") double longitude,
            @Parameter(name = "radius", description = "검색 반경(미터 단위)", example = "1000") @RequestParam(value = "radius", defaultValue = "1000") int radius,
            @Parameter(hidden = true) Pageable pageable // Pageable은 Swagger에서 자동으로 파라미터들을 생성해줍니다.
    );
}





