package org.traffichunter.javaagent.testai.persistence.jpa;

import java.time.LocalDateTime;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.traffichunter.javaagent.testai.domain.Chats;

public interface JpaChatRepository extends JpaRepository<Chats, Long> {

    boolean existsByCreatedAtAndThreadId(LocalDateTime lastTimeCreatedAt, Long threadId);

    boolean existsByThreadId(Long threadId);
}
