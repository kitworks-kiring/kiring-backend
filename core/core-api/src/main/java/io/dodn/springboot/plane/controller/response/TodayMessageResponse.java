package io.dodn.springboot.plane.controller.response;

public record TodayMessageResponse(
        boolean hasTodayMessage
) {
    public static TodayMessageResponse of(final boolean todayMessage) {
        return new TodayMessageResponse(todayMessage);
    }
}
