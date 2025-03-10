package org.traffichunter.javaagent.testai.core.repository;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.traffichunter.javaagent.testai.domain.Threads;

public interface ThreadRepository {

    Threads save(Threads thread);

    Threads findById(Long id);

    Threads findByUserId(Long userId);

    Threads findByUserIdAndThreadId(Long userId, Long threadId);

    boolean existsUserIdAndThreadId(Long userId, Long threadId);

    boolean existsByUserId(Long userId);

    void deleteByUserId(Long userId);

    boolean existsByLatestHalfHourCreateAt(LocalDateTime halfHour);

    Page<Threads> findAll(Pageable pageable);
}
