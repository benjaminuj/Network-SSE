package com.network.sse.transport.service;

import com.network.sse.chat_message.domain.ChatMessage;
import com.network.sse.transport.infrastructure.EmitterRepository;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransportService {
    private static final Long SSE_TIMEOUT = 1000 * 5L;
    private final EmitterRepository emitterRepository;

    /**
     * emitter id 접두사 생성
     * @param chatRoomId sse에 연결할 채팅방 id
     * @param userId sse에 연결할 유저 id
     * @return emitterId 접두사
     */
    private String createEmitterIdPrefix(Long chatRoomId, Long userId) {
        return chatRoomId + "_" + userId;
    }

    /**
     * emitter id에서 접두사 추출
     * @param emitterId SseEmitter Id
     * @return emitterId 접두사
     */
    private String getEmitterIdPrefix(String emitterId) {
        int prefixEndIdx = emitterId.lastIndexOf("_");
        return emitterId.substring(0, prefixEndIdx);
    }

    /**
     * 유저가 채팅 방 입장시 sse 연결
     * @param chatRoomId sse에 연결할 채팅방 id
     * @param userId sse에 연결할 유저 id
     * @param lastEventId lastEventId 클라이언트가 마지막으로 수신한 eventId
     * @return SseEmitter 객체
     */
    public SseEmitter connectSseForChat(Long chatRoomId, Long userId, String lastEventId) {
        String emitterIdPrefix = createEmitterIdPrefix(chatRoomId, userId);
        String emitterId =  emitterIdPrefix + "_" + System.currentTimeMillis();
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(SSE_TIMEOUT));

        // 연결 종료 시
        emitter.onCompletion(() -> {
            log.info("SSE 연결 종료 (모든 종료 사유 포함): emitterId: " + emitterId);
            emitterRepository.deleteEmitterById(emitterId); // 객체 삭제
        });
        // 타임아웃 발생 시
        emitter.onTimeout(() -> {
            log.warn("SSE 타임아웃 발생: emitterId: " + emitterId);
            emitterRepository.deleteEmitterById(emitterId); // 객체 삭제
        });
        // 에러 발생 시
        emitter.onError((e) -> {
            log.error("SSE 연결 중 오류 발생: " + e.getMessage() + ", emitterId: " + emitterId);
            emitterRepository.deleteEmitterById(emitterId); // 객체 삭제
        });

        // 503 error 방지 차원에서 더미 이벤트 전송
        if (lastEventId == null || lastEventId.isEmpty()) {
            String startEventId = emitterIdPrefix+"_EventStreamCreated";
            sendToClient(emitter, emitterId, startEventId, "EventStream Created... [chatRoomId=%d userId=%d]".formatted(chatRoomId, userId));
        }

        // 미수신 데이터 전송
        if (!lastEventId.isEmpty()) {
            sendLostData(lastEventId, emitterId, emitter);
        }

        return emitter;
    }

    /**
     * 데이터 전송
     * @param emitter 사용할 SseEmitter 객체
     * @param emitterId SseEmitter를 관리하는 id
     * @param eventId 데이터 id
     * @param data 전송할 데이터 (채팅 메시지...)
     */
    private void sendToClient(SseEmitter emitter, String emitterId, String eventId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .name("chat")
                    .data(data));
        } catch (IOException | IllegalStateException exception) {
            log.warn("SSE send() 실패: 클라이언트와의 연결이 이미 종료되었을 가능성이 있습니다. event id: " + eventId);
            emitterRepository.deleteEmitterById(emitterId);
        } catch (Exception exception) {
            log.error("예상치 못한 오류 발생: " + exception.getMessage());
            emitterRepository.deleteEmitterById(emitterId);
            throw new RuntimeException("SSE 연결에 알 수 없는 오류가 발생했습니다!", exception);
        }
    }

    /**
     * 미수신 데이터 전송
     * @param lastEventId 마지막으로 수신한 이벤트 id
     * @param emitterId SseEmitter id
     * @param emitter SseEmitter 객체
     */
    private void sendLostData(String lastEventId, String emitterId, SseEmitter emitter) {
        String emitterIdPrefix = getEmitterIdPrefix(emitterId);
        Map<String, Object> events = emitterRepository.findAllEventCacheStartWithId(emitterIdPrefix);

        events.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // 전송 순서 보장
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> {
                    ChatMessage chatMessage = (ChatMessage) entry.getValue();
                    sendToClient(emitter, emitterId, entry.getKey(), chatMessage.getContent());
                });
    }

    /**
     * sse로 실시간 채팅 메시지 전송
     * @param chatRoomId 채팅방 id
     * @param receiverId 수신자 user id
     * @param chatMessage 메시지
     */
    public void sendMessage(Long chatRoomId, Long receiverId, ChatMessage chatMessage) {
        // 데이터를 받아야 하는 유저의 id (receiverId)에 해당하는 emitter
        String emitterIdPrefix = createEmitterIdPrefix(chatRoomId, receiverId);
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterStartWithId(emitterIdPrefix);

        String eventId = emitterIdPrefix + "_" + System.currentTimeMillis();

        // 데이터 유실에 대비해 캐시에 데이터 저장
        emitterRepository.saveEventCache(eventId, chatMessage);

        // 데이터 전송
        sseEmitters.forEach(
                (key, emitter) -> {
                    sendToClient(emitter, key, eventId, chatMessage.getContent());
                }
        );
    }

    /*모든 SseEmitter 조회*/
    public Map<String, String> getAllEmitters() {
        return emitterRepository.getAllEmitters().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().toString()
                ));
    }

    /*모든 event cache 조회*/
    public Map<String, Object> getAllEventCache() {
        return emitterRepository.getAllEventCache();
    }
}