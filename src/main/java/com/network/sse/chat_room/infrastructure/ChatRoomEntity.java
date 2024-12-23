package com.network.sse.chat_room.infrastructure;

import com.network.sse.chat_room.domain.ChatRoom;
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
@Table(name = "ChatRoom")
public class ChatRoomEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerId", nullable = false)
    private UserEntity owner;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    public static ChatRoomEntity fromModel(ChatRoom chatRoom) {
        ChatRoomEntity chatRoomEntity = new ChatRoomEntity();

        if (chatRoom.getId() != null) {
            chatRoomEntity.id = chatRoom.getId(); // 이미 id가 부여된 엔티티를 모델로 쓰다가 다시 엔티티로 변경하는 경우, id가 초기화 되는 것 방지
        }
        chatRoomEntity.owner = UserEntity.fromModel(chatRoom.getOwner());
        chatRoomEntity.name = chatRoom.getName();

        return chatRoomEntity;
    }

    public ChatRoom toModel() {
        // Entity를 Model로 변환한다
        ChatRoom chatRoom = ChatRoom.builder()
                .owner(owner.toModel())
                .name(name)
                .build();

        // id 할당
        chatRoom.assignId(id);

        // Model의 정보를 DB 정보와 동기화한다
        chatRoom.syncWithPersistence(getCreatedAt(), getUpdatedAt(), getStatus());
        return chatRoom;
    }
}
