package com.network.sse.user.service;

import com.network.sse.common.domain.Status;
import com.network.sse.common.domain.exception.ResourceNotFoundException;
import com.network.sse.user.domain.User;
import com.network.sse.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findByIdAndStatus(Long id, Status status) {
        return userRepository.findByIdAndStatus(id, status).orElseThrow(() -> new ResourceNotFoundException("User", id));
    }
}
