package org.traffichunter.javaagent.testai.persistence;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.traffichunter.javaagent.testai.core.repository.ThreadRepository;
import org.traffichunter.javaagent.testai.domain.Threads;
import org.traffichunter.javaagent.testai.persistence.jpa.JpaThreadRepository;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ThreadRepositoryImpl implements ThreadRepository {

    private final JpaThreadRepository jpaThreadRepository;

    @Override
    @Transactional
    public Threads save(final Threads thread) {
        return jpaThreadRepository.save(thread);
    }

    @Override
    public Threads findById(final Long id) {
        return jpaThreadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Thread not found"));
    }

    @Override
    @Transactional
    public void deleteByUserId(final Long userId) {
        jpaThreadRepository.deleteByUserId(userId);
    }

    @Override
    public Threads findByUserIdAndThreadId(final Long userId, final Long threadId) {
        return jpaThreadRepository.findByUserIdAndId(userId, threadId)
                .orElseThrow(() -> new IllegalArgumentException("Thread not found"));
    }

    @Override
    public boolean existsUserIdAndThreadId(final Long userId, final Long threadId) {
        return jpaThreadRepository.existsByUserIdAndId(userId, threadId);
    }

    @Override
    public Threads findByUserId(final Long userId) {
        return jpaThreadRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Thread not found"));
    }

    @Override
    public boolean existsByUserId(final Long userId) {
        return jpaThreadRepository.existsByUserId(userId);
    }

    @Override
    public boolean existsByLatestHalfHourCreateAt(final LocalDateTime halfHour) {
        return jpaThreadRepository.existsByLastChatAt(halfHour);
    }

    @Override
    public Page<Threads> findAll(final Pageable pageable) {
        return jpaThreadRepository.findAll(pageable);
    }
}
