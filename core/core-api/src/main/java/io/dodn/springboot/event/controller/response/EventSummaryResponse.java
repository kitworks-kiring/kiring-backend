package io.dodn.springboot.event.controller.response;

import io.dodn.springboot.storage.db.event.entity.Event;
import io.dodn.springboot.storage.db.member.entity.Member;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.MonthDay;

public record EventSummaryResponse(
        Long eventId,
        String eventType, // 예: "STUDY", "DINNER", "BIRTHDAY"
        String title,
        LocalDateTime start,
        LocalDateTime end,
        String creatorName
) {

    public static EventSummaryResponse fromEvent(Event event) {
        return new EventSummaryResponse(
                event.getId(),
                event.getEventCategory().name(),
                event.getTitle(),
                event.getStartDatetime(),
                event.getEndDatetime(),
                event.getCreator().getName()
        );
    }

    /**
     * Member 엔티티의 생일 정보를 요약 DTO로 변환합니다.
     * @param member 생일인 멤버
     * @param eventYear 해당 년도
     */
    public static EventSummaryResponse fromBirthday(Member member, int eventYear) {
        // "MM-dd" 형식의 문자열을 MonthDay 객체로 파싱
        MonthDay birthdayMonthDay = MonthDay.parse("--" + member.getBirthday());
        // 주어진 년도와 조합하여 해당 년도의 생일 날짜 생성
        LocalDate birthdayThisYear = birthdayMonthDay.atYear(eventYear);

        return new EventSummaryResponse(
                null, // 생일은 Event 테이블에 ID가 없으므로 null
                "BIRTHDAY", // 이벤트 타입을 "BIRTHDAY"로 지정
                member.getName() + "님 생일", // 예: "김태민님 생일"
                birthdayThisYear.atStartOfDay(), // 하루 종일 이벤트로 처리
                birthdayThisYear.plusDays(1).atStartOfDay().minusNanos(1),
                null // 생성자가 없으므로 null
        );
    }
}
