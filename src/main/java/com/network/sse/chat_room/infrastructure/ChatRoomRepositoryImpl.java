package com.network.sse.chat_room.infrastructure;

import com.network.sse.chat_room.domain.ChatRoom;
import com.network.sse.chat_room.service.port.ChatRoomRepository;
import com.network.sse.common.domain.Status;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepository {
    private final ChatRoomJpaRepository chatRoomJpaRepository;

    @Override
    public Optional<ChatRoom> findByIdAndStatus(Long id, Status status) {
        return chatRoomJpaRepository.findByIdAndStatus(id, status).map(ChatRoomEntity::toModel);
    }

    @Override
    public ChatRoom save(ChatRoom chatRoom) {
        return chatRoomJpaRepository.save(ChatRoomEntity.fromModel(chatRoom)).toModel();
    }
}
