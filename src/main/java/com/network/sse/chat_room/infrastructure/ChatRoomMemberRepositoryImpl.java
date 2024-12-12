package com.network.sse.chat_room.infrastructure;

import com.network.sse.chat_room.domain.ChatRoomMember;
import com.network.sse.chat_room.service.port.ChatRoomMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatRoomMemberRepositoryImpl implements ChatRoomMemberRepository {
    private final ChatRoomMemberJpaRepository chatRoomMemberJpaRepository;

    @Override
    public ChatRoomMember save(ChatRoomMember chatRoomMember) {
        return chatRoomMemberJpaRepository.save(ChatRoomMemberEntity.fromModel(chatRoomMember)).toModel();
    }
}
