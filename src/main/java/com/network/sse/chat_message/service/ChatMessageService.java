package com.network.sse.chat_message.service;

import com.network.sse.chat_message.domain.ChatMessage;
import com.network.sse.chat_message.domain.dto.PostSendChatMessage;
import com.network.sse.chat_message.service.port.ChatMessageRepository;
import com.network.sse.chat_room.domain.ChatRoom;
import com.network.sse.chat_room.service.ChatRoomService;
import com.network.sse.common.domain.Status;
import com.network.sse.common.domain.exception.ResourceNotFoundException;
import com.network.sse.user.domain.User;
import com.network.sse.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatRoomService chatRoomService;
    private final UserService userService;

    private final ChatMessageRepository chatMessageRepository;

    /*메시지 전송*/
    public ChatMessage saveChatMessage(PostSendChatMessage postSendChatMessage) {
        // 요청받은 id값을 가진 각 객체 조회
        ChatRoom chatRoom = chatRoomService.findByIdAndStatus(postSendChatMessage.getChatRoomId(), Status.ACTIVE);
        User sender = userService.findByIdAndStatus(postSendChatMessage.getSenderId(), Status.ACTIVE);
        User receiver = userService.findByIdAndStatus(postSendChatMessage.getReceiverId(), Status.ACTIVE);

        // 채팅 메시지 객체 생성 및 저장
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .receiver(receiver)
                .content(postSendChatMessage.getMessageContent())
                .isRemoved(false)
                .build();
        return chatMessageRepository.save(chatMessage);
    }

    /*메시지 조회*/
    public ChatMessage getChatMessage(long messageId) {
        return chatMessageRepository.findByIdAndStatus(messageId, Status.ACTIVE).orElseThrow(() -> new ResourceNotFoundException("User", messageId));
    }
}
