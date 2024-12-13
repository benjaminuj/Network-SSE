package com.network.sse.user.domain;

import com.network.sse.common.domain.Base;
import lombok.Builder;
import lombok.Getter;

@Getter
public class User extends Base {
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private String profileImage;

    @Builder
    public User(String email, String password, String nickname, String profileImage) {
        if (email == null || password == null || nickname == null) {
            throw new IllegalArgumentException("필수 필드는 null일 수 없습니다.");
        }
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    // 영속화 후 ID 할당
    public void assignId(Long id) {
        this.id = id;
    }
}
