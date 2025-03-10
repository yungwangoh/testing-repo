package org.traffichunter.javaagent.testai.core;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.traffichunter.javaagent.testai.core.dto.ChatResponse;
import org.traffichunter.javaagent.testai.core.model.Model;
import org.traffichunter.javaagent.testai.core.repository.ChatRepository;
import org.traffichunter.javaagent.testai.core.repository.ThreadRepository;
import org.traffichunter.javaagent.testai.domain.Chats;
import org.traffichunter.javaagent.testai.domain.Threads;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ThreadRepository threadRepository;

    private final ChatRepository chatRepository;

    private final List<Model> models;

    @Transactional
    public ChatResponse create(final Long userId,
                               final String question,
                               final boolean isStreaming,
                               final String model) {

        Threads threads = threadRepository.findByUserId(userId);

        // return map to chat after select model
        Chats chats = models.stream()
                .filter(element -> Objects.equals(element.getModel(), model))
                .map(element -> Chats.builder()
                        .question(question)
                        .answer(element.execute(question, isStreaming))
                        .question(question)
                        .thread(threads)
                        .createdAt(LocalDateTime.now())
                        .deleted(false)
                        .build()
                )
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Model " + model + " not found"));

        return ChatResponse.create(chats);
    }

    public Page<ChatResponse> findAllThreadGroup(final Long threadId, final Pageable pageable) {

        List<ChatResponse> chatResponses = chatRepository.pageByThreadId(threadId, pageable)
                .map(ChatResponse::create)
                .toList();

        return new PageImpl<>(chatResponses);
    }

    public Page<ChatResponse> findAllThreadGroup(final Long userId, final Long threadId, final Pageable pageable) {

        boolean isCheck = threadRepository.existsUserIdAndThreadId(userId, threadId);

        if(!isCheck) {
            throw new IllegalStateException("Thread not found");
        }

        List<ChatResponse> chatResponses = chatRepository.pageByThreadId(threadId, pageable)
                .map(ChatResponse::create)
                .toList();

        return new PageImpl<>(chatResponses);
    }
}
