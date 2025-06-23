package io.dodn.springboot.plane.controller.response;

import io.dodn.springboot.plane.domain.PlaneStatusInfo;

public record PlaneStatusResponse(
        int todaySentCount,
        boolean hasTodayReceived,
        RecommendedMemberResponse todayRecommendation
) {
    public static PlaneStatusResponse from(
            final PlaneStatusInfo planeStatusInfo
    ) {
        return new PlaneStatusResponse(
                planeStatusInfo.todaySentCount(),
                planeStatusInfo.hasTodayReceived(),
                planeStatusInfo.todayRecommendation()
        );
    }
}
