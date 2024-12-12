package com.network.sse.user.infrastructure;

import com.network.sse.common.domain.Status;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByIdAndStatus(Long id, Status status);
}
