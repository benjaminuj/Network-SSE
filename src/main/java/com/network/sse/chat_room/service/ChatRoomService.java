package com.network.sse.chat_room.service;

import com.network.sse.chat_room.domain.ChatRoom;
import com.network.sse.chat_room.domain.ChatRoomMember;
import com.network.sse.chat_room.service.port.ChatRoomMemberRepository;
import com.network.sse.chat_room.service.port.ChatRoomRepository;
import com.network.sse.common.domain.Status;
import com.network.sse.common.domain.exception.ResourceNotFoundException;
import com.network.sse.user.domain.User;
import com.network.sse.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final UserService userService;

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    public ChatRoom findByIdAndStatus(Long id, Status status) {
        return chatRoomRepository.findByIdAndStatus(id, status).orElseThrow(() -> new ResourceNotFoundException("ChatRoom", id));
    }

    /*채팅 방 입장*/
    public void enterChatRoom(Long chatRoomId, Long userId) {
        // 요청받은 id값을 가진 각 객체 조회
        ChatRoom chatRoom = findByIdAndStatus(chatRoomId, Status.ACTIVE);
        User user = userService.findByIdAndStatus(userId, Status.ACTIVE);

        // '채팅방 멤버' 객체 생성 및 저장
        ChatRoomMember chatRoomMember = ChatRoomMember.builder()
                .room(chatRoom)
                .member(user)
                .isExited(false)
                .build();
        chatRoomMemberRepository.save(chatRoomMember);
    }
}
