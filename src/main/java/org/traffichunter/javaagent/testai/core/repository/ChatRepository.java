package org.traffichunter.javaagent.testai.core.repository;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.traffichunter.javaagent.testai.domain.Chats;

public interface ChatRepository {

    Chats save(Chats chats);

    Chats findById(Long id);

    //  before last time 30 min
    boolean existsByLastCreatedAtAndThreadId(LocalDateTime lastCreatedAt, Long threadId);

    boolean existsByThreadId(Long threadId);

    Page<Chats> pageByThreadId(Long threadId, Pageable pageable);
}
