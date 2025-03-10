package org.traffichunter.javaagent.testai.core.dto;

public record UserRequest(
        String email,
        String password,
        String name
) {
}
