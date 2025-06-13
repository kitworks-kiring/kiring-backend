package io.dodn.springboot.event.controller.response;

import io.dodn.springboot.storage.db.common.entity.EventCategory;
import io.dodn.springboot.storage.db.event.entity.Event;
import io.dodn.springboot.storage.db.member.entity.Member;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.MonthDay;

public record CalendarEventResponse(
        Long eventId,
        String eventType,
        String title,
        LocalDateTime start,
        LocalDateTime end
) {

    public static CalendarEventResponse fromEvent(Event event) {
        return new CalendarEventResponse(
                event.getId(),
                event.getEventCategory().name(), // Enum의 이름을 문자열로 사용
                event.getTitle(),
                event.getStartDatetime(),
                event.getEndDatetime()
        );
    }

    public static CalendarEventResponse fromBirthday(Member member, int year) {
        // 생일은 하루 종일 지속되는 이벤트로 처리
        MonthDay birthdayMonthDay = MonthDay.parse("--" + member.getBirthday());

        LocalDate birthdayThisYear = birthdayMonthDay.atYear(year);

        // 생일은 하루 종일 지속되는 이벤트로 처리
        LocalDateTime birthdayStart = birthdayThisYear.atStartOfDay();
        LocalDateTime birthdayEnd = birthdayStart.plusDays(1).minusNanos(1);

        return new CalendarEventResponse(
                null, // 생일은 Event 테이블에 ID가 없으므로 null
                EventCategory.BIRTHDAY.name(),
                member.getName(),
                birthdayStart,
                birthdayEnd
        );
    }


}
