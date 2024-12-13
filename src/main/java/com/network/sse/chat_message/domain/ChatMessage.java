package com.network.sse.chat_message.domain;

import com.network.sse.chat_room.domain.ChatRoom;
import com.network.sse.common.domain.Base;
import com.network.sse.common.domain.Status;
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
    private boolean isRemoved = false;
    private LocalDateTime deletedAt;

    @Builder
    public ChatMessage(ChatRoom chatRoom, User sender, User receiver, String content, boolean isRemoved, LocalDateTime deletedAt) {
        if (chatRoom == null || sender == null || receiver == null || content == null) {
            throw new IllegalArgumentException("필수 필드는 null일 수 없습니다.");
        }
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

    public void remove() {
        this.isRemoved = true;
        this.deletedAt = LocalDateTime.now();
        this.status = Status.INACTIVE;
    }
}
