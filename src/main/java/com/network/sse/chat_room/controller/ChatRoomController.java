package com.network.sse.chat_room.controller;

import com.network.sse.chat_room.service.ChatRoomService;
import com.network.sse.common.domain.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("chat-room")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

     /*채팅 방 입장*/
    @PostMapping("/enter/{chat-room-id}/{user-id}")
    public BaseResponse<String> enterChatRoom(@PathVariable(name = "chat-room-id") Long chatRoomId,
                                @PathVariable(name = "user-id") Long userId
    ) {
        chatRoomService.enterChatRoom(chatRoomId, userId);

        return new BaseResponse<>("요청에 성공하였습니다.", HttpStatus.OK.value(), userId + "번 유저가 " + chatRoomId + "번 채팅방에 입장했습니다.");
    }
}
