package com.network.sse.chat_message.infrastructure;

import com.network.sse.chat_message.domain.ChatMessage;
import com.network.sse.chat_message.service.port.ChatMessageRepository;
import com.network.sse.common.domain.Status;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepository {
    private final ChatMessageJpaRepository chatMessageJpaRepository;

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        return chatMessageJpaRepository.save(ChatMessageEntity.fromModel(chatMessage)).toModel();
    }

    @Override
    public Optional<ChatMessage> findByIdAndStatus(Long id, Status status) {
        return chatMessageJpaRepository.findByIdAndStatus(id, status).map(ChatMessageEntity::toModel);
    }
}