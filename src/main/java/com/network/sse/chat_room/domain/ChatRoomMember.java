package com.network.sse.chat_room.domain;

import com.network.sse.common.domain.Base;
import com.network.sse.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatRoomMember extends Base {
    private Long id;
    private ChatRoom room;
    private User member;
    private boolean isExited = false;

    @Builder
    public ChatRoomMember(Long id, ChatRoom room, User member, boolean isExited) {
        this.id = id;
        this.room = room;
        this.member = member;
        this.isExited = isExited;
    }

    // 영속화 후 ID 할당
    public void assignId(Long id) {
        this.id = id;
    }
}
