package org.traffichunter.javaagent.testai.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.traffichunter.javaagent.testai.annotation.User;
import org.traffichunter.javaagent.testai.core.ChatService;
import org.traffichunter.javaagent.testai.core.UserService;
import org.traffichunter.javaagent.testai.core.dto.ChatRequest;
import org.traffichunter.javaagent.testai.core.dto.ChatResponse;

/**
 * User annotation is method argument resolver.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/test/chats/")
public class ChatApi {

    private final ChatService chatService;

    private final UserService userService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ChatResponse createChatApi(@User final Long userId,
                                      @RequestBody final ChatRequest chatRequest) {

        return chatService.create(
                userId,
                chatRequest.question(),
                chatRequest.isStreaming(),
                chatRequest.model()
        );
    }

    @GetMapping("/{threadId}")
    @ResponseStatus(HttpStatus.OK)
    public Page<ChatResponse> findThreadGroupChatApi(@User final Long userId,
                                                     @PathVariable("threadId") final Long threadId,
                                                     @PageableDefault(
                                                             sort = "created_at",
                                                             direction = Direction.DESC
                                                     ) final Pageable pageable) {

        boolean isAdmin = userService.isAdmin(userId);

        // admin find all
        if(isAdmin) {
            return chatService.findAllThreadGroup(threadId, pageable);
        }

        return chatService.findAllThreadGroup(userId, threadId, pageable);
    }
}
