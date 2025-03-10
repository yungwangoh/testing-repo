package org.traffichunter.javaagent.testai.core;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.traffichunter.javaagent.testai.core.dto.UserResponse;
import org.traffichunter.javaagent.testai.core.repository.UserRepository;
import org.traffichunter.javaagent.testai.domain.Role;
import org.traffichunter.javaagent.testai.domain.Users;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse signUp(final String email, final String password, final String name) {

        Users users = new Users(
                email,
                password,
                name,
                Role.MEMBER,
                false,
                LocalDateTime.now()
        );

        return UserResponse.from(userRepository.save(users));
    }

    public boolean isAdmin(final Long userId) {

        Users user = userRepository.findById(userId);

        return user.getRole() == Role.ADMIN;
    }
}
