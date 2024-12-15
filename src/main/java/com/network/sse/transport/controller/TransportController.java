package com.network.sse.transport.controller;

import com.network.sse.common.domain.BaseResponse;
import com.network.sse.transport.service.TransportService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("events")
@RequiredArgsConstructor
public class TransportController {
    private final TransportService transportService;

    /**
     * 채팅방에 입장한 유저 sse 연결
     * @param chatRoomId sse에 연결할 채팅방 id
     * @param userId sse에 연결할 유저 id
     * @param lastEventId 클라이언트가 마지막으로 수신한 eventId
     * @return SseEmitter 객체
     */
    @GetMapping(value = "/{chat-room-id}/{user-id}", produces = "text/event-stream")
    public SseEmitter connectSseForChat (@PathVariable(name = "chat-room-id") Long chatRoomId,
                                @PathVariable(name = "user-id") Long userId,
                                @RequestHeader(value = "Last-Event-ID", defaultValue = "") String lastEventId) {
        return transportService.connectSseForChat(chatRoomId, userId, lastEventId);
    }

    /*모든 SseEmitter 조회*/
    @GetMapping("/emitters")
    public BaseResponse<Map<String, String>> getEmitters() {
        Map<String, String> result =  transportService.getAllEmitters();

        return new BaseResponse<>("요청에 성공하였습니다.", HttpStatus.OK.value(), result);
    }

    /*모든 event cache 조회*/
    @GetMapping("/event-cache")
    public BaseResponse<Map<String, Object>> getEventCache() {
        Map<String, Object> result =  transportService.getAllEventCache();

        return new BaseResponse<>("요청에 성공하였습니다.", HttpStatus.OK.value(), result);
    }
}
