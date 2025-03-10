package org.traffichunter.javaagent.testai.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.traffichunter.javaagent.testai.annotation.User;
import org.traffichunter.javaagent.testai.core.ThreadService;
import org.traffichunter.javaagent.testai.core.dto.ThreadResponse;
import org.traffichunter.javaagent.testai.core.dto.UserResponse;
import org.traffichunter.javaagent.testai.domain.Threads;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test/threads/")
public class ThreadApi {

    private final ThreadService threadService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ThreadResponse createThreadApi(@User final Long userId) {

        Threads threads = threadService.create(userId);

        return new ThreadResponse(
                UserResponse.from(threads.getUser()),
                threads.isDeleted(),
                threads.getCreateAt()
        );
    }

    @DeleteMapping("/{threadId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteThreadApi(@User final Long userId, @PathVariable final Long threadId) {
        threadService.deleteThread(userId, threadId);
    }
}
