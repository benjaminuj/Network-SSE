package com.network.sse.chat_message.service.port;

import com.network.sse.chat_message.domain.ChatMessage;
import com.network.sse.common.domain.Status;
import java.util.Optional;

public interface ChatMessageRepository {
    ChatMessage save(ChatMessage chatMessage);
    Optional<ChatMessage> findByIdAndStatus(Long id, Status status);
}
