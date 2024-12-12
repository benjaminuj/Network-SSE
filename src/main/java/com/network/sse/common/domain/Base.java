package com.network.sse.common.domain;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class Base {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    protected Status status;

    // DB 정보와 동기화
    public void syncWithPersistence(LocalDateTime createdAt, LocalDateTime updatedAt, Status status) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
    }
}
