package com.network.sse.transport.service;

import com.network.sse.chat_message.domain.ChatMessage;
import com.network.sse.transport.infrastructure.EmitterRepository;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class TransportService {
    private static final Long SSE_TIMEOUT = 1000 * 10L;
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

        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        if (lastEventId == null || lastEventId.isEmpty()) {
            // 503 error 방지 차원에서 더미 이벤트 전송
            sendToClient(emitter, emitterId, "EventStream Created... [chatRoomId=%d userId=%d]".formatted(chatRoomId, userId));
        }

        return emitter;
    }

    /**
     * 데이터 전송
     * @param emitter 사용할 SseEmitter 객체
     * @param id SseEmitter id
     * @param data 전송할 데이터 (채팅 메시지...)
     */
    private void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("chat")
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(id);
            throw new RuntimeException("SSE 연결에 오류가 발생했습니다!");
        }
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

        sseEmitters.forEach(
                (key, emitter) -> {
                    // 데이터 전송
                    sendToClient(emitter, eventId, chatMessage.getContent());
                }
        );
    }
}