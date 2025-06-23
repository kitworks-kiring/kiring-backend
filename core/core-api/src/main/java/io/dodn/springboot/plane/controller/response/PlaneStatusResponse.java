package io.dodn.springboot.plane.controller.response;

import io.dodn.springboot.plane.domain.PlaneStatusInfo;
import io.dodn.springboot.storage.db.member.entity.Member;

public record PlaneStatusResponse(
        int todaySentCount,
        boolean hasTodayReceived,
        Member todayRecommendation
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
