package com.network.sse.user.infrastructure;

import com.network.sse.common.domain.Status;
import com.network.sse.user.domain.User;
import com.network.sse.user.service.port.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findByIdAndStatus(Long id, Status status) {
        return userJpaRepository.findByIdAndStatus(id, status).map(UserEntity::toModel);
    }
}
