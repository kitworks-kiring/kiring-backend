package io.dodn.springboot.event.domain;

import io.dodn.springboot.event.controller.response.CalendarEventResponse;
import io.dodn.springboot.storage.db.event.EventRepository;
import io.dodn.springboot.storage.db.event.entity.Event;
import io.dodn.springboot.storage.db.member.MemberRepository;
import io.dodn.springboot.storage.db.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
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
}
