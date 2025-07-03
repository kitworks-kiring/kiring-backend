package io.dodn.springboot.sse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalTime;
import java.util.stream.Stream;

@RestController
public class SseController {
    private static final Logger log = LoggerFactory.getLogger(SseController.class);

    private final SseService sseService;

    public SseController(final SseService sseService) {
        this.sseService = sseService;
    }

    @GetMapping(path = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> sse() {
        log.info("SSE 요청이 들어왔습니다.");

        Flux.interval(Duration.ofSeconds(10))
                .map(i -> ServerSentEvent.builder().comment("keep-alive").build());

        return Flux.interval(Duration.ofSeconds(1)) // 1초마다 이벤트 발생
                .map(sequence -> ServerSentEvent.<String>builder()
                        .id(String.valueOf(sequence))
                        .event("message") // 이벤트 이름 지정
                        .data("서버 시간: " + LocalTime.now().toString())
                        .comment("실시간 데이터 전송 중") // 주석
                        .build());
    }

    /**
     * AI 답변처럼 텍스트를 한 글자씩 스트리밍하는 SSE 엔드포인트
     */
    @GetMapping(path = "/stream-text", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamText() {
        String sampleText = "안녕하세요! Spring Boot와 Next.js를 이용한 SSE 통신 예제입니다. 이 텍스트는 AI가 답변하는 것처럼 보이기 위해 한 글자씩 전송됩니다.";

        // 1. 텍스트를 한 글자씩 쪼개어 Stream<String>을 생성합니다.
        Stream<String> textStream = sampleText.chars().mapToObj(c -> String.valueOf((char) c));

        // 2. 50ms(0.05초) 간격으로 이벤트를 발생시키는 Flux를 생성합니다. (타이핑 속도 조절)
        Flux<Long> interval = Flux.interval(Duration.ofMillis(50));

        // 3. 텍스트 스트림과 시간 간격 Flux를 합칩니다.
        //    interval에 맞춰 텍스트를 하나씩 방출합니다.
        return Flux.fromStream(textStream)
                .zipWith(interval, (text, tick) -> text) // 텍스트만 취함
                .map(character -> ServerSentEvent.<String>builder()
                        .id(String.valueOf(System.currentTimeMillis()))
                        .event("message") // 프론트에서 받을 이벤트 이름
                        .data(character)  // 한 글자씩 데이터로 전송
                        .build())
                // 4. 스트림이 끝나면, 종료를 알리는 이벤트를 추가로 보냅니다.
                .concatWith(Flux.just(ServerSentEvent.<String>builder()
                        .event("end")
                        .data("[DONE]") // 종료 신호
                        .build()));
    }

    @GetMapping(path = "/realtime-ai", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamRealtimeAi() {
        return sseService.createStream();
    }
}
