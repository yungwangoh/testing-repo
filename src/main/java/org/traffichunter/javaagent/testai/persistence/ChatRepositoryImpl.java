package org.traffichunter.javaagent.testai.persistence;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.traffichunter.javaagent.testai.core.repository.ChatRepository;
import org.traffichunter.javaagent.testai.domain.Chats;
import org.traffichunter.javaagent.testai.persistence.jpa.JpaChatRepository;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRepositoryImpl implements ChatRepository {

    private final JpaChatRepository jpaChatRepository;

    @Override
    @Transactional
    public Chats save(final Chats chats) {
        return jpaChatRepository.save(chats);
    }

    @Override
    public Chats findById(final Long id) {
        return jpaChatRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found chat"));
    }

    @Override
    public boolean existsByThreadId(final Long threadId) {
        return jpaChatRepository.existsByThreadId(threadId);
    }

    @Override
    public boolean existsByLastCreatedAtAndThreadId(final LocalDateTime lastCreatedAt, final Long threadId) {
        return jpaChatRepository.existsByCreatedAtAndThreadId(lastCreatedAt, threadId);
    }

    @Override
    public Page<Chats> pageByThreadId(final Long threadId, final Pageable pageable) {
        return null;
    }
}
