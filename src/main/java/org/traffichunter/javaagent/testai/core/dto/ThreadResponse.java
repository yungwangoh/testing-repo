package org.traffichunter.javaagent.testai.core.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import org.traffichunter.javaagent.testai.domain.Threads;

@Builder
public record ThreadResponse(
        UserResponse userResponse,
        boolean delete,
        LocalDateTime createdAt
) {

    public static ThreadResponse from(final Threads thread) {
        return ThreadResponse.builder()
                .userResponse(UserResponse.from(thread.getUser()))
                .delete(thread.isDeleted())
                .createdAt(thread.getCreateAt())
                .build();
    }
}
