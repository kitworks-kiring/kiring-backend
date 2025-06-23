package io.dodn.springboot.plane.domain;

import io.dodn.springboot.storage.db.member.entity.Member;

public record PlaneStatusInfo(
        int todaySentCount,
        boolean hasTodayReceived,
        Member todayRecommendation
) {
    public static PlaneStatusInfo of(final long sentCount, final boolean receivedMessage, final Member recommendedMember) {
        return new PlaneStatusInfo(
                (int) sentCount,
                receivedMessage,
                recommendedMember
        );

    }
}
