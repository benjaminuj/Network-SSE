package com.network.sse.transport.infrastructure;

import java.util.Map;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EmitterRepository {
    SseEmitter save(String emitterId, SseEmitter sseEmitter);
    Map<String, SseEmitter> findAllEmitterStartWithId(String id);
    void deleteEmitterById(String emitterId);
    void saveEventCache(String eventCacheId, Object event);
    Map<String, Object> findAllEventCacheStartWithId(String id);
    void deleteEventCacheById(String eventCacheId);
    Map<String, SseEmitter> getAllEmitters();
    Map<String, Object> getAllEventCache();
}
