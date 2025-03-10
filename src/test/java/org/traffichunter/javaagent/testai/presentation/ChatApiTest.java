package org.traffichunter.javaagent.testai.presentation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.traffichunter.javaagent.testai.AbstractTestConfigration;
import org.traffichunter.javaagent.testai.config.resolver.UserMethodArgumentResolver;
import org.traffichunter.javaagent.testai.core.ChatService;
import org.traffichunter.javaagent.testai.core.UserService;
import org.traffichunter.javaagent.testai.core.dto.ChatRequest;
import org.traffichunter.javaagent.testai.core.dto.ChatResponse;
import org.traffichunter.javaagent.testai.presentation.advice.GlobalAdvice;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = ChatApi.class)
class ChatApiTest extends AbstractTestConfigration {

    private MockMvc mockMvc;

    @MockitoBean
    ChatService chatService;

    @MockitoBean
    UserService userService;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ChatApi(chatService, userService))
                .setCustomArgumentResolvers(
                        new UserMethodArgumentResolver(),
                        new PageableHandlerMethodArgumentResolver()
                )
                .setControllerAdvice(new GlobalAdvice())
                .build();
    }

    @Test
    void 유저는_대화를_생성할_수_있다_201() throws Exception {
        // given
        ChatResponse chatResponse = ChatResponse.builder()
                .threadId(1L)
                .question("안녕")
                .answer("안녕하세요")
                .deleted(false)
                .answerCreatedAt(LocalDateTime.now())
                .build();

        ChatRequest chatRequest = ChatRequest.builder()
                .question("안녕")
                .model("gpt")
                .isStreaming(false)
                .build();

        given(chatService.create(anyLong(), anyString(), anyBoolean(), anyString()))
                .willReturn(chatResponse);

        // when
        ResultActions resultActions = mockMvc.perform(post("/test/chats/")
                .content(mapper.writeValueAsString(chatRequest))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void 유저는_자신이_생성한_대화만_조회할_수_있다_400() throws Exception {
        // given
        given(userService.isAdmin(anyLong())).willReturn(false);
        given(chatService.findAllThreadGroup(anyLong(), anyLong(), any()))
                .willThrow(new IllegalStateException("Thread not found"));

        // when
        ResultActions resultActions = mockMvc.perform(get("/test/chats/{threadId}", 1L)
                .param("page", "0")
                .param("size", "10")
                .param("sort", "created_at,DESC"));

        // then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 관리자는_모든_thread를_조회할_수_있다_200() throws Exception {
        // given
        List<ChatResponse> results = getChatList();
        given(userService.isAdmin(anyLong())).willReturn(true);
        given(chatService.findAllThreadGroup(anyLong(), any()))
                .willReturn(new PageImpl<>(results, PageRequest.of(0, 10), results.size()));

        // when
        ResultActions resultActions = mockMvc.perform(get("/test/chats/{threadId}", 1L)
                .param("page", "0")
                .param("size", "10")
                .param("sort", "created_at,DESC"));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    private List<ChatResponse> getChatList() {
        return List.of(
                ChatResponse.builder()
                        .threadId(1L)
                        .question("안녕")
                        .answer("안녕하세요")
                        .deleted(false)
                        .answerCreatedAt(LocalDateTime.now())
                        .build(),

                ChatResponse.builder()
                        .threadId(1L)
                        .question("안녕~")
                        .answer("안녕하세요~~")
                        .deleted(false)
                        .answerCreatedAt(LocalDateTime.now())
                        .build()
        );
    }
}