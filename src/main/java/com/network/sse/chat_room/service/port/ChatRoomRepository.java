package com.network.sse.chat_room.service.port;

import com.network.sse.chat_room.domain.ChatRoom;
import com.network.sse.common.domain.Status;
import java.util.Optional;

public interface ChatRoomRepository {
    Optional<ChatRoom> findByIdAndStatus(Long id, Status status);
    ChatRoom save(ChatRoom chatRoom);
}
