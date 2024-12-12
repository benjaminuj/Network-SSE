package com.network.sse.user.infrastructure;

import com.network.sse.common.infrastructure.BaseEntity;
import com.network.sse.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "User")
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, length = 30)
    private String email;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname;

    @Column(name = "profile_image", length = 2000)
    private String profileImage;

    public static UserEntity fromModel(User user) {
        UserEntity userEntity = new UserEntity();

        userEntity.id = user.getId();
        userEntity.email = user.getEmail();
        userEntity.password = user.getPassword();
        userEntity.nickname = user.getNickname();
        userEntity.profileImage = user.getProfileImage();

        return userEntity;
    }

    public User toModel() {
        // Entity를 Model로 변환한다
        User user = User.builder()
                .id(id)
                .email(email)
                .password(password)
                .nickname(nickname)
                .profileImage(profileImage)
                .build();

        // Model의 정보를 DB 정보와 동기화한다
        user.syncWithPersistence(getCreatedAt(), getUpdatedAt(), getStatus());
        return user;
    }
}
