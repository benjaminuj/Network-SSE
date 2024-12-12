package com.network.sse.chat_room.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomMemberJpaRepository extends JpaRepository<ChatRoomMemberEntity, Long> {
}
