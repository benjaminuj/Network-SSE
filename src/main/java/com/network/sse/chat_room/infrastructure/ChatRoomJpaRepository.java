package com.network.sse.chat_room.infrastructure;

import com.network.sse.common.domain.Status;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoomEntity, Long> {
    Optional<ChatRoomEntity> findByIdAndStatus(Long id, Status status);
}
