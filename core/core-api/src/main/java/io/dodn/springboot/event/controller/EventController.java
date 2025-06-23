package io.dodn.springboot.event.controller;

import io.dodn.springboot.common.support.response.ApiResponse;
import io.dodn.springboot.common.swagger.EventDocs;
import io.dodn.springboot.event.controller.response.CalendarEventResponse;
import io.dodn.springboot.event.controller.response.DailyEventsResponse;
import io.dodn.springboot.event.domain.EventService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/event")
public class EventController implements EventDocs {
    private final EventService eventService;

    public EventController(final EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/monthly")
    public ApiResponse<List<CalendarEventResponse>> getMonthlyCalendar(
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "month", required = false) Integer month
    ) {
        LocalDate today = LocalDate.now();
        int queryYear = (year != null) ? year : today.getYear();
        int queryMonth = (month != null) ? month : today.getMonthValue();

        List<CalendarEventResponse> monthlyEvents = eventService.getMonthlyEvents(queryYear, queryMonth);
        return ApiResponse.success(monthlyEvents);
    }

    @GetMapping("/weekly")
    public ApiResponse<List<DailyEventsResponse>> getWeeklyCalendar() {
        List<DailyEventsResponse> weeklyEvents = eventService.getWeeklyEvents();
        return ApiResponse.success(weeklyEvents);
    }


}
