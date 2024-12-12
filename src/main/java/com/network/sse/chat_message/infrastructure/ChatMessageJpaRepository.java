package com.network.sse.chat_message.infrastructure;

import com.network.sse.common.domain.Status;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessageEntity, Long> {
    Optional<ChatMessageEntity> findByIdAndStatus(Long id, Status status);
}
