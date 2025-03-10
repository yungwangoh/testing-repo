package org.traffichunter.javaagent.testai.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.traffichunter.javaagent.testai.core.repository.ChatRepository;
import org.traffichunter.javaagent.testai.core.repository.ThreadRepository;
import org.traffichunter.javaagent.testai.core.repository.UserRepository;
import org.traffichunter.javaagent.testai.domain.Role;
import org.traffichunter.javaagent.testai.domain.Threads;
import org.traffichunter.javaagent.testai.domain.Users;

@ExtendWith(MockitoExtension.class)
class ThreadServiceTest {

    @InjectMocks
    private ThreadService threadService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ThreadRepository threadRepository;

    @Mock
    private ChatRepository chatRepository;

    @Test
    void 기존_쓰레드가_삭제된_경우_예외가_발생한다() {
        // given
        Long userId = 1L;
        Users users = new Users("email", "pwd", "name", Role.MEMBER, false, LocalDateTime.now());
        Threads deletedThread = Threads.builder()
                .user(users)
                .deleted(true)
                .createAt(LocalDateTime.now())
                .build();

        given(threadRepository.findByUserId(userId)).willReturn(deletedThread);

        // when & then
        assertThatThrownBy(() -> threadService.create(userId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Thread is deleted");
    }

    @Test
    void 첫_대화이고_사용자가_30분_이내에_대화를_하지_않았다면_새로운_쓰레드를_생성한다() {
        // given
        Long userId = 1L;
        Users users = new Users("email", "pwd", "name", Role.MEMBER, false, LocalDateTime.now());
        Threads oldThread = Threads.builder()
                .user(users)
                .deleted(false)
                .createAt(LocalDateTime.now().minusHours(2))
                .build();

        given(threadRepository.findByUserId(userId)).willReturn(oldThread);
        given(chatRepository.existsByThreadId(oldThread.getId())).willReturn(true);
        given(chatRepository.existsByLastCreatedAtAndThreadId(any(LocalDateTime.class), eq(oldThread.getId())))
                .willReturn(false);
        given(userRepository.findById(userId)).willReturn(users);
        given(threadRepository.save(any(Threads.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        Threads newThread = threadService.create(userId);

        // then
        assertThat(newThread).isNotEqualTo(oldThread);
        assertThat(newThread.getUser()).isEqualTo(users);
    }
}