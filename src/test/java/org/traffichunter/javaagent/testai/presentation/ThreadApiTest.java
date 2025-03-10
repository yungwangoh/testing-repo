package org.traffichunter.javaagent.testai.presentation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.traffichunter.javaagent.testai.AbstractTestConfigration;
import org.traffichunter.javaagent.testai.config.resolver.UserMethodArgumentResolver;
import org.traffichunter.javaagent.testai.core.ThreadService;
import org.traffichunter.javaagent.testai.core.dto.ThreadResponse;
import org.traffichunter.javaagent.testai.core.repository.ChatRepository;
import org.traffichunter.javaagent.testai.core.repository.ThreadRepository;
import org.traffichunter.javaagent.testai.core.repository.UserRepository;
import org.traffichunter.javaagent.testai.domain.Role;
import org.traffichunter.javaagent.testai.domain.Threads;
import org.traffichunter.javaagent.testai.domain.Users;
import org.traffichunter.javaagent.testai.presentation.advice.GlobalAdvice;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = ThreadApi.class)
class ThreadApiTest extends AbstractTestConfigration {

    private MockMvc mockMvc;

    @MockitoBean
    ThreadService threadService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ThreadApi(threadService))
                .setCustomArgumentResolvers(
                        new UserMethodArgumentResolver(),
                        new PageableHandlerMethodArgumentResolver()
                )
                .setControllerAdvice(new GlobalAdvice())
                .build();
    }

    @Test
    void 다른_유저가_자신이_작성한_쓰레드를_삭제_하려고_한다_404() throws Exception {
        // given
        willThrow(new IllegalArgumentException("Thread not found"))
                .given(threadService)
                .deleteThread(anyLong(), anyLong());

        // when
        ResultActions resultActions = mockMvc.perform(delete("/test/threads/{threadId}", 2L));

        // then
        resultActions.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void 유저의_첫_질문이거나_마지막_질문_후_30분이_지난_경우_새로운_쓰레드_생성한다_201() throws Exception {
        // given
        Threads newThread = Threads.builder()
                .user(new Users("email", "pwd", "name", Role.MEMBER, false, LocalDateTime.now()))
                .deleted(false)
                .build();

        given(threadService.create(anyLong())).willReturn(newThread);

        // when
        ResultActions resultActions = mockMvc.perform(post("/test/threads/"));

        // then
        resultActions.andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void 유저의_첫_질문이거나_마지막_질문_후_30분이_지난_경우가_아니면_기존_쓰레드를_반환한다_201() throws Exception {
        // given
        Threads thread = Threads.builder()
                .user(new Users("email", "pwd", "name", Role.MEMBER, false, LocalDateTime.now()))
                .deleted(false)
                .build();

        given(threadService.create(anyLong())).willReturn(thread);

        // when
        ResultActions resultActions = mockMvc.perform(post("/test/threads/"));

        // then
        resultActions.andExpect(status().isCreated())
                .andDo(print());
    }
}