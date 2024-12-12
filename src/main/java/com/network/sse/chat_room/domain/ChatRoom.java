package com.network.sse.chat_room.domain;

import com.network.sse.common.domain.Base;
import com.network.sse.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatRoom extends Base {
    private Long id;
    private User owner;
    private String name;

    @Builder
    public ChatRoom(Long id, User owner, String name) {
        this.id = id;
        this.owner = owner;
        this.name = name;
    }

    // 영속화 후 ID 할당
    public void assignId(Long id) {
        this.id = id;
    }
}
