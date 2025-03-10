package org.traffichunter.javaagent.testai.core.dto;

import lombok.Builder;

@Builder
public record ChatRequest(
        String question,
        boolean isStreaming,
        String model
) {
}
