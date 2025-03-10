package org.traffichunter.javaagent.testai.persistence.jpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.traffichunter.javaagent.testai.domain.Threads;

public interface JpaThreadRepository extends JpaRepository<Threads, Long> {

    void deleteByUserId(Long userId);

    Optional<Threads> findByUserId(Long userId);

    boolean existsByLastChatAt(LocalDateTime lastChatAt);

    Optional<Threads> findByUserIdAndId(Long userId, Long threadId);

    boolean existsByUserIdAndId(Long userId, Long threadId);

    boolean existsByUserId(Long userId);
}
