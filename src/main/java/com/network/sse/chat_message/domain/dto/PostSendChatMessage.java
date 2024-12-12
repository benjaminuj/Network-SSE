package com.network.sse.chat_message.domain.dto;

import lombok.Getter;

@Getter
public class PostSendChatMessage {
    private long chatRoomId;
    private long senderId;
    private long receiverId;
    private String messageContent;
}
