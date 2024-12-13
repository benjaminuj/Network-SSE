package com.network.sse.chat_room.infrastructure;

import com.network.sse.chat_room.domain.ChatRoomMember;
import com.network.sse.common.infrastructure.BaseEntity;
import com.network.sse.user.infrastructure.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ChatRoomMember")
public class ChatRoomMemberEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomId", nullable = false)
    private ChatRoomEntity room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private UserEntity member;

    @Column(name = "isExited", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isExited = false;

    public static ChatRoomMemberEntity fromModel(ChatRoomMember chatRoomMember) {
        ChatRoomMemberEntity chatRoomMemberEntity = new ChatRoomMemberEntity();

        if (chatRoomMember.getId() != null) {
            chatRoomMemberEntity.id = chatRoomMember.getId(); // 이미 id가 부여된 엔티티를 모델로 쓰다가 다시 엔티티로 변경하는 경우, id가 초기화 되는 것 방지
        }
        chatRoomMemberEntity.room = ChatRoomEntity.fromModel(chatRoomMember.getRoom());
        chatRoomMemberEntity.member = UserEntity.fromModel(chatRoomMember.getMember());
        chatRoomMemberEntity.isExited = chatRoomMember.isExited();

        return chatRoomMemberEntity;
    }

    public ChatRoomMember toModel() {
        // Entity를 Model로 변환한다
        ChatRoomMember chatRoomMember = ChatRoomMember.builder()
                .room(room.toModel())
                .member(member.toModel())
                .isExited(isExited)
                .build();

        // id 할당
        chatRoomMember.assignId(id);

        // Model의 정보를 DB 정보와 동기화한다
        chatRoomMember.syncWithPersistence(getCreatedAt(), getUpdatedAt(), getStatus());
        return chatRoomMember;
    }
}
