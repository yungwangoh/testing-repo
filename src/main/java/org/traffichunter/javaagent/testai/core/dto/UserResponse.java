package org.traffichunter.javaagent.testai.core.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import org.traffichunter.javaagent.testai.domain.Role;
import org.traffichunter.javaagent.testai.domain.Users;

@Builder
public record UserResponse(
        String email,
        String name,
        Role role,
        boolean deleted,
        LocalDateTime createdAt
) {

    public static UserResponse from(final Users users) {
        return UserResponse.builder()
                .email(users.getEmail())
                .name(users.getName())
                .role(users.getRole())
                .deleted(users.isDeleted())
                .createdAt(users.getCreatedAt())
                .build();
    }
}
