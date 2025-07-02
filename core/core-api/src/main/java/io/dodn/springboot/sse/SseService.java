package io.dodn.springboot.sse;

import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;


@Service
public class SseService {
    private static final String PADDING = " ".repeat(4096);

    // 각 SSE 스트림에 대한 고유한 큐를 생성하기 위해 BlockingQueue를 사용합니다.
    public Flux<ServerSentEvent<String>> createStream() {

        // 스레드로부터 안전한 큐 생성
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();

        // 1. 생산자 (Producer) - AI 모델 시뮬레이션
        // 별도의 스레드에서 텍스트를 생성하고 큐에 넣습니다.
        startProducer(queue);

        // 2. 소비자 (Consumer) - SSE 이벤트 스트림 생성
        return Flux.<ServerSentEvent<String>>create(sink -> {
                    while (!sink.isCancelled()) {
                        try {
                            // 큐에서 데이터를 꺼냅니다. 데이터가 없으면 대기(block)합니다.
                            String token = queue.take();

                            // 종료 신호 확인
                            if ("[DONE]".equals(token)) {
                                // 종료 이벤트를 보내고 스트림을 완료합니다.
                                sink.next(ServerSentEvent.<String>builder().event("end").data("[DONE]").build());
                                sink.complete();
                                break; // 루프 종료
                            }

                            // 일반 데이터(토큰)를 SSE 이벤트로 만들어 보냅니다.
                            sink.next(ServerSentEvent.<String>builder()
                                    .event("message")
                                    .data(token + PADDING)
                                    .build());

                        } catch (InterruptedException e) {
                            // 스레드가 중단되면 에러를 발생시키고 종료합니다.
                            sink.error(e);
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                })
                // 클라이언트 연결이 끊어지는 등 취소 이벤트가 발생했을 때 로깅
                .doOnCancel(() -> System.out.println("Stream cancelled by client."))
                // 이 Flux가 별도의 스레드에서 실행되도록 스케줄러를 지정합니다.
                .subscribeOn(Schedulers.boundedElastic());
    }

    private void startProducer(BlockingQueue<String> queue) {
        String fullText = "이 예제는 BlockingQueue를 사용하여 생산자-소비자 패턴을 구현한 실시간 텍스트 스트리밍 데모입니다. AI가 실시간으로 답변을 생성하는 것처럼 보이죠.";
        String[] tokens = fullText.split(""); // 한 글자씩 쪼개기

        // 별도의 스레드에서 작업을 실행합니다.
        new Thread(() -> {
            try {
                for (String token : tokens) {
                    // 실제 AI처럼 불규칙한 속도로 생성되는 것을 흉내 냅니다.
                    Thread.sleep(ThreadLocalRandom.current().nextInt(50, 200));
                    queue.put(token); // 큐에 데이터 추가
                }
                // 모든 토큰을 보낸 후, 종료 신호를 큐에 추가합니다.
                queue.put("[DONE]");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}