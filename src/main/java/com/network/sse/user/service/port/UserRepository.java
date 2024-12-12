package com.network.sse.user.service.port;

import com.network.sse.common.domain.Status;
import com.network.sse.user.domain.User;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByIdAndStatus(Long id, Status status);
}
