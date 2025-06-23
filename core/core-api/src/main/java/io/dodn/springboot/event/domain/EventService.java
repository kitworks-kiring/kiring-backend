package io.dodn.springboot.event.domain;

import io.dodn.springboot.event.controller.response.CalendarEventResponse;
import io.dodn.springboot.event.controller.response.DailyEventsResponse;
import io.dodn.springboot.event.controller.response.EventSummaryResponse;
import io.dodn.springboot.storage.db.event.EventRepository;
import io.dodn.springboot.storage.db.event.entity.Event;
import io.dodn.springboot.storage.db.member.MemberRepository;
import io.dodn.springboot.storage.db.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final MemberRepository memberRepository;

    public EventService(final EventRepository eventRepository, final MemberRepository memberRepository) {
        this.eventRepository = eventRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public List<CalendarEventResponse> getMonthlyEvents(
            final int year,
            final int month
    ) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = yearMonth.atEndOfMonth().plusDays(1).atStartOfDay().minusDays(1);

        List<Event> regularEvents = eventRepository.findEventsForPeriod(startDate, endDate);

        // ★★★ 월(int)을 두 자리 문자열(예: "06")로 포맷팅하여 전달 ★★★
        String monthString = String.format("%02d", month);
        List<Member> birthdayMembers = memberRepository.findMembersWithBirthdayInMonth(monthString);

        Stream<CalendarEventResponse> regularEventStream = regularEvents.stream()
                .map(CalendarEventResponse::fromEvent);

        Stream<CalendarEventResponse> birthdayEventStream = birthdayMembers.stream()
                .map(member -> CalendarEventResponse.fromBirthday(member, year));

        return Stream.concat(birthdayEventStream, regularEventStream)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DailyEventsResponse> getWeeklyEvents() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = startOfWeek.plusWeeks(1);

        // 2. Event 테이블에서 이번 주에 해당하는 일반 일정을 조회합니다.
        List<Event> regularEvents = eventRepository.findEventsForPeriod(startOfWeek.atStartOfDay(), endOfWeek.atStartOfDay());

        // 3. DB에서 모든 멤버 정보를 가져옵니다. (팀별로 필터링 가능)
        List<Member> allMembers = memberRepository.findAll();

        // 4. 조회된 일반 일정을 DTO 스트림으로 변환합니다.
        Stream<EventSummaryResponse> regularEventStream = regularEvents.stream()
                .map(EventSummaryResponse::fromEvent);

        // 5. 모든 멤버를 순회하며 이번 주에 생일이 있는 멤버를 찾아 DTO 스트림으로 변환합니다.
        Stream<EventSummaryResponse> birthdayEventStream = allMembers.stream()
                .filter(member -> {
                    if (member.getBirthday() == null || member.getBirthday().isBlank()) {
                        return false;
                    }
                    MonthDay birthdayMonthDay = MonthDay.parse("--" + member.getBirthday());
                    LocalDate birthdayThisYear = birthdayMonthDay.atYear(today.getYear());
                    // 생일이 이번 주 범위에 포함되는지 확인
                    return !birthdayThisYear.isBefore(startOfWeek) && birthdayThisYear.isBefore(endOfWeek);
                })
                .map(member -> EventSummaryResponse.fromBirthday(member, today.getYear()));

        // 6. 두 스트림을 합치고 날짜별로 그룹핑하기 위해 DTO 리스트로 변환합니다.
        List<EventSummaryResponse> allEvents = Stream.concat(regularEventStream, birthdayEventStream)
                .sorted(Comparator.comparing(EventSummaryResponse::start))
                .toList();

        // 7. 최종적으로 날짜별로 그룹핑된 응답 DTO를 생성하여 반환합니다.
        return DailyEventsResponse.from(allEvents);
    }
}
