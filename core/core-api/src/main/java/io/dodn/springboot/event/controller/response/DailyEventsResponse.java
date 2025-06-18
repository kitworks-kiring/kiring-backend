package io.dodn.springboot.event.controller.response;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record DailyEventsResponse(
        LocalDate date,
        List<EventSummaryResponse> events
) {
    public static List<DailyEventsResponse> from(List<EventSummaryResponse> allEvents) {
        // 모든 이벤트를 날짜(LocalDate)별로 그룹핑합니다.
        return allEvents.stream()
                .collect(Collectors.groupingBy(event -> event.start().toLocalDate()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new DailyEventsResponse(entry.getKey(), entry.getValue()))
                .toList();
    }
}
