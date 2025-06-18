package io.dodn.springboot.common.swagger;

import io.dodn.springboot.common.support.response.ApiResponse;
import io.dodn.springboot.event.controller.response.CalendarEventResponse;
import io.dodn.springboot.event.controller.response.DailyEventsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface EventDocs {
    @Operation(summary = "월간 이벤트 조회", description = "월간 이벤트 데이터 조회", tags = { "Event Management" })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = CalendarEventResponse.class))),
    })
    public ApiResponse<List<CalendarEventResponse>> getMonthlyCalendar(
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "month", required = false) Integer month
    );

    @Operation(summary = "주간 이벤트 조회", description = "주간 이벤트 데이터 조회", tags = { "Event Management" })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = DailyEventsResponse.class))),
    })
    public ApiResponse<List<DailyEventsResponse>> getWeeklyCalendar();

}
