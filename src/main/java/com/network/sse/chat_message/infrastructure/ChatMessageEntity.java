package com.network.sse.chat_message.infrastructure;

import com.network.sse.chat_message.domain.ChatMessage;
import com.network.sse.chat_room.infrastructure.ChatRoomEntity;
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
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ChatMessage")
public class ChatMessageEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomId", nullable = false)
    private ChatRoomEntity chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senderId", nullable = false)
    private UserEntity sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiverId", nullable = false)
    private UserEntity receiver;

    @Column(name = "content", nullable = false, length = 2000)
    private String content;

    @Column(name = "isRemoved", nullable = false)
    private boolean isRemoved;

    @Column(name = "deletedAt")
    private LocalDateTime deletedAt;

    public static ChatMessageEntity fromModel(ChatMessage chatMessage) {
        ChatMessageEntity chatMessageEntity = new ChatMessageEntity();

        chatMessageEntity.id = chatMessage.getId();
        chatMessageEntity.chatRoom = ChatRoomEntity.fromModel(chatMessage.getChatRoom());
        chatMessageEntity.sender = UserEntity.fromModel(chatMessage.getSender());
        chatMessageEntity.receiver = UserEntity.fromModel(chatMessage.getReceiver());
        chatMessageEntity.content = chatMessage.getContent();
        chatMessageEntity.isRemoved = chatMessage.isRemoved();
        chatMessageEntity.deletedAt = chatMessage.getDeletedAt();

        return chatMessageEntity;
    }

    public ChatMessage toModel() {
        // Entity를 Model로 변환한다
        ChatMessage chatMessage = ChatMessage.builder()
                .id(id)
                .chatRoom(chatRoom.toModel())
                .sender(sender.toModel())
                .receiver(receiver.toModel())
                .content(content)
                .isRemoved(isRemoved)
                .deletedAt(deletedAt)
                .build();

        // Model의 정보를 DB 정보와 동기화한다
        chatMessage.syncWithPersistence(getCreatedAt(), getUpdatedAt(), getStatus());
        return chatMessage;
    }
}
