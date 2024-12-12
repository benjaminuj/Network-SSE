package com.network.sse.chat_message.controller;

import com.network.sse.chat_message.domain.ChatMessage;
import com.network.sse.chat_message.domain.dto.PostSendChatMessage;
import com.network.sse.chat_message.service.ChatMessageService;
import com.network.sse.common.domain.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("chat-message")
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    /*메시지 전송*/
    @PostMapping("")
    public BaseResponse<String> sendChatMessage(
            @RequestBody PostSendChatMessage postSendChatMessage
            ) {
        // 채팅 메시지 객체 생성 및 저장
        ChatMessage chatMessage = chatMessageService.saveChatMessage(postSendChatMessage);

        return new BaseResponse<>("요청에 성공하였습니다.", HttpStatus.OK.value(), chatMessage.getId()+"번의 메시지가 전송되었습니다.");
    }

    /*특정 메시지 조회*/
    @GetMapping("/{message-id}")
    public BaseResponse<String> getChatMessage(
            @PathVariable(name = "message-id")
            long messageId
    ) {
        ChatMessage chatMessage = chatMessageService.getChatMessage(messageId);

        return new BaseResponse<>("요청에 성공하였습니다.", HttpStatus.OK.value(), chatMessage.getContent());
    }
}
