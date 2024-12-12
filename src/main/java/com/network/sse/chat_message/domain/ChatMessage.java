package com.network.sse.chat_message.domain;

import com.network.sse.chat_room.domain.ChatRoom;
import com.network.sse.common.domain.Base;
import com.network.sse.user.domain.User;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatMessage extends Base {
    private Long id;
    private ChatRoom chatRoom;
    private User sender;
    private User receiver;
    private String content;
    private boolean isRemoved;
    private LocalDateTime deletedAt;

    @Builder
    public ChatMessage(Long id, ChatRoom chatRoom, User sender, User receiver, String content, boolean isRemoved, LocalDateTime deletedAt) {
        this.id = id;
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.isRemoved = isRemoved;
        this.deletedAt = deletedAt;
    }

    // 영속화 후 ID 할당
    public void assignId(Long id) {
        this.id = id;
    }
}
