package com.network.sse.transport.infrastructure;

import java.util.Map;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EmitterRepository {
    SseEmitter save(String emitterId, SseEmitter sseEmitter);
    Map<String, SseEmitter> findAllEmitterStartWithId(String id);
    void deleteById(String emitterId);
}
