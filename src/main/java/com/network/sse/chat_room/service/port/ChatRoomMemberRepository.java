package com.network.sse.chat_room.service.port;

import com.network.sse.chat_room.domain.ChatRoomMember;

public interface ChatRoomMemberRepository {
    ChatRoomMember save(ChatRoomMember chatRoomMember);
}
