package com.soloproject.todoApp.todo.controller;

import com.google.gson.Gson;
import com.soloproject.todoApp.todo.dto.TodoDto;
import com.soloproject.todoApp.todo.entity.Todo;
import com.soloproject.todoApp.todo.mapper.TodoMapper;
import com.soloproject.todoApp.todo.service.TodoService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import static com.soloproject.todoApp.todo.util.ApiDocumentUtils.getRequestPreProcessor;
import static com.soloproject.todoApp.todo.util.ApiDocumentUtils.getResponsePreProcessor;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)  // Controller를 테스트 하기 위한 구조
@MockBean(JpaMetamodelMappingContext.class)  // JPA에서 사용하는 Bean들을 Mock 객체로 주입하는 설정
@AutoConfigureRestDocs // Spring Rest Docs에 대한 자동 구성
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private TodoService todoService;

    @MockBean
    private TodoMapper mapper;

    @Test
    public void postTodoTest() throws Exception {
        // given
        TodoDto.Post post = new TodoDto.Post("운동하기", 1, false);
        String content = gson.toJson(post);  // json으로

        // stubbing
        given(mapper.TodoPostDtoToTodo(Mockito.any(TodoDto.Post.class))).willReturn(new Todo());

        Todo mockResultTodo = new Todo();
        mockResultTodo.setId(1L);
        // Stubbing
        given(todoService.createTodo(Mockito.any(Todo.class))).willReturn(mockResultTodo);

        // when
        ResultActions actions =
                mockMvc.perform(
                post("/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        // then
        actions
                .andDo(print())
                .andExpect(status().isCreated())
                //.andExpect(header().string("Location", is(startsWith(""))))
                //========= API 문서화 관련 코드 ===========//
                .andDo(document("post-todo",  // 식별자
                      getRequestPreProcessor(),  // 문서 전처리
                      getResponsePreProcessor(), // 문서 전처리
                        /** request body **/
                      requestFields(
                              List.of(
                                      fieldWithPath("title").type(JsonFieldType.STRING).description("할일 제목"),
                                      fieldWithPath("todoOrder").type(JsonFieldType.NUMBER).description("우선 순위"),
                                      fieldWithPath("completed").type(JsonFieldType.BOOLEAN).description("할일 완료 여부")
                              )
                      ),
                      /** response header **/
                      responseHeaders(
                            headerWithName(HttpHeaders.LOCATION).description("Location header. 등록된 리소스의 URI")
                      )
                ));
        //========== API 문서화 관련 코드 끝 =========//
    }

    @Test
    void getTodosTest() throws Exception {

        // given
        Todo todo1 = new Todo("운동하기", 1, true);
        Todo todo2 = new Todo("공부하기", 2, false);

        List<Todo> list = new ArrayList<>();
        list.add(todo1);
        list.add(todo2);

        List<TodoDto.Repsonse> responses = List.of(
                new TodoDto.Repsonse(
                        1L,
                        "운동하기",
                        1,
                        true
                ),
                new TodoDto.Repsonse(
                        2L,
                        "공부하기",
                        2,
                        false
                )
        );

        // Stubbing 메서드
        given(todoService.findTodos()).willReturn(list);

        given(mapper.TodosTodoResponseDtos(Mockito.anyList())).willReturn(responses);

        // when
        ResultActions actions = mockMvc.perform(
                get("/")
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        actions.andDo(print())
                .andExpect(status().isOk())
                // 배열 형태
                .andExpect(jsonPath("$").isArray())
                .andDo(document("get-todos",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        /** response body **/
                        responseFields(
                                List.of(
                                        fieldWithPath("[]").type(JsonFieldType.ARRAY).description("결과 데이터"),
                                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("할일 식별자"),
                                        fieldWithPath("[].title").type(JsonFieldType.STRING).description("할일 제목"),
                                        fieldWithPath("[].todoOrder").type(JsonFieldType.NUMBER).description("우선 순위"),
                                        fieldWithPath("[].completed").type(JsonFieldType.BOOLEAN).description("할일 완료 여부")
                                )
                        )
                ));
    }

    @Test
    void getTodoTest() throws Exception{

        // given
        long Id = 1L;
        Todo todo = new Todo("운동하기", 1, true);
        todo.setId(Id);

        TodoDto.Repsonse response = new TodoDto.Repsonse(
                1L,
                "운동하기",
                1,
                true
        );

        // Stubbing 메서드
        given(todoService.findTodo(Mockito.anyLong())).willReturn(new Todo());

        given(mapper.TodoToTodoResponseDto(Mockito.any(Todo.class))).willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(
                get("/{id}", Id)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then (응답만)
        actions
                .andDo(print())
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.id").value(todo.getId()))
                .andExpect(jsonPath("$.title").value(todo.getTitle()))
                .andExpect(jsonPath("$.todoOrder").value(todo.getTodoOrder()))
                .andExpect(jsonPath("$.completed").value(todo.isCompleted()))
                .andDo(document("get-todo",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("id").description("todo 식별자")
                        ),
                        /** Response body **/
                        responseFields(
                                List.of(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("할일 식별자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("할일 제목"),
                                        fieldWithPath("todoOrder").type(JsonFieldType.NUMBER).description("우선 순위"),
                                        fieldWithPath("completed").type(JsonFieldType.BOOLEAN).description("할일 완료 여부")
                                )
                        )

                ));
    }

    @Test
    public void patchTodoTest() throws Exception{

        // given
        long Id = 1L;
        TodoDto.Patch patch = new TodoDto.Patch( "운동하기", 1, true);
        patch.addId(Id);

        String content = gson.toJson(patch);

        TodoDto.Repsonse repsonseDto =
                new TodoDto.Repsonse(1L,
                        "운동하기"
                        , 1,
                        true);

        // stubbing
        given(mapper.TodoPatchDtoToTodo(Mockito.any(TodoDto.Patch.class))).willReturn(new Todo());

        given(todoService.updateTodo(Mockito.any(Todo.class))).willReturn(new Todo());

        given(mapper.TodoToTodoResponseDto(Mockito.any(Todo.class))).willReturn(repsonseDto);


        // when
        ResultActions actions = mockMvc.perform(
                patch("/{id}", Id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        // then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                // 같은지 확인..
                .andExpect(jsonPath("$.id").value(patch.getId()))
                .andExpect(jsonPath("$.title").value(patch.getTitle()))
                .andExpect(jsonPath("$.todoOrder").value(patch.getTodoOrder()))
                .andExpect(jsonPath("$.completed").value(patch.isCompleted()))
                .andDo(document("patch-todo",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        /** 파라미터 **/
                        pathParameters(
                                parameterWithName("id").description("todo 할일 식별자")
                        ),
                        /** Reqeust body **/
                        requestFields(
                                List.of(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("할일 식별자").ignored(),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("할일 제목").optional(),
                                        fieldWithPath("todoOrder").type(JsonFieldType.NUMBER).description("우선 순위").optional(),
                                        fieldWithPath("completed").type(JsonFieldType.BOOLEAN).description("할일 완료 여부").optional()
                                )
                        ),
                        /** Response body **/
                        responseFields(
                                List.of(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("할일 식별자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("할일 제목"),
                                        fieldWithPath("todoOrder").type(JsonFieldType.NUMBER).description("우선 순위"),
                                        fieldWithPath("completed").type(JsonFieldType.BOOLEAN).description("할일 완료 여부")
                                )
                        )
                ));
    }

    @Test
    void deleteTodosTest() throws Exception{

        // given
        long Id1 = 1L;
        long Id2 = 2L;

        // stubbing 메서드
        doNothing().when(todoService).deleteTodos();

        // when
        ResultActions actions = mockMvc.perform(
                delete("/")
        );

        // then
        actions.andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("delete-todos",
                        getRequestPreProcessor(),
                        getResponsePreProcessor()
                ));
    }

    @Test
    void deleteTodoTest() throws Exception{

        // given
        long Id = 1L;
        
        // stubbing 메서드
        doNothing().when(todoService).deleteTodo(Id);

        // when
        ResultActions actions = mockMvc.perform(
                delete("/{id}", Id)
        );

        // then
        actions.andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("delete-todo",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("id").description("todo 식별자")
                        )
                ));
    }
}