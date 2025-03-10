package org.traffichunter.javaagent.testai.core;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.traffichunter.javaagent.testai.core.repository.ChatRepository;
import org.traffichunter.javaagent.testai.core.repository.ThreadRepository;
import org.traffichunter.javaagent.testai.core.repository.UserRepository;
import org.traffichunter.javaagent.testai.domain.Threads;
import org.traffichunter.javaagent.testai.domain.Users;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ThreadService {

    private final UserRepository userRepository;

    private final ThreadRepository threadRepository;

    private final ChatRepository chatRepository;

    @Transactional
    public Threads create(final Long userId) {

        // user first question
        Threads thread = threadRepository.findByUserId(userId);

        if(thread.isDeleted()) {
            throw new IllegalStateException("Thread is deleted");
        }

        boolean isUserCheck = chatRepository.existsByThreadId(thread.getId());

        // last question 30 min
        boolean isHalfHourCheck = chatRepository.existsByLastCreatedAtAndThreadId(
                LocalDateTime.now().minusMinutes(30),
                thread.getId()
        );

        if(isUserCheck || !isHalfHourCheck) {

            Users user = userRepository.findById(userId);

            Threads threads = Threads.builder()
                    .user(user)
                    .createAt(LocalDateTime.now())
                    .deleted(false)
                    .build();

            return threadRepository.save(threads);

        } else {
            return thread;
        }
    }

    @Transactional
    public void deleteThread(final Long userId, final Long threadId) {
        Threads threads = threadRepository.findByUserIdAndThreadId(userId, threadId);
        threads.deleteThread();
    }
}
