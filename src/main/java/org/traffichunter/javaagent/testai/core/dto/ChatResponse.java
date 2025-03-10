package org.traffichunter.javaagent.testai.core.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import org.traffichunter.javaagent.testai.domain.Chats;

@Builder
public record ChatResponse(
        Long threadId,
        String question,
        String answer,
        boolean deleted,
        LocalDateTime answerCreatedAt) {

    public static ChatResponse create(final Chats chats) {
        return ChatResponse.builder()
                .threadId(chats.getThread().getId())
                .question(chats.getQuestion())
                .answer(chats.getAnswer())
                .deleted(false)
                .answerCreatedAt(LocalDateTime.now())
                .build();
    }
}
