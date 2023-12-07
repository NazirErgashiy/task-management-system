package uz.nazir.task.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import uz.nazir.task.IntegrationTestInitializer;
import uz.nazir.task.configs.jwt.JwtService;
import uz.nazir.task.dto.request.TaskDtoRequest;
import uz.nazir.task.dto.security.request.RegisterRequest;
import uz.nazir.task.entities.enums.Priority;
import uz.nazir.task.entities.enums.Status;
import uz.nazir.task.validators.RoleValidator;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@Order(2)
@ContextConfiguration(initializers = IntegrationTestInitializer.class)
@SpringBootTest
@WebAppConfiguration
class TaskControllerTest {

    private MockMvc mockMvc;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private SecurityContextHolder securityContextHolder;
    @MockBean
    private RoleValidator validator;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeTestClass
    public void setupMocks() {
        validator = Mockito.mock(RoleValidator.class);
        doThrow(new RuntimeException()).when(validator).canEditOnlySelfElements(anyLong(), any());
        doThrow(new RuntimeException()).when(validator).canEditOnlySelfElements(anyString(), any());
        doThrow(new RuntimeException()).when(validator).availableRoles(any());
    }

    @BeforeEach
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext)
                .build();
    }

    /**
     * 200
     */
    @Test
    void readPaged() throws Exception {
        mockMvc.perform(get("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", notNullValue()));
    }

    /**
     * 201
     */
    @Test
    void create() throws Exception {

        RegisterRequest registerRequest1 = RegisterRequest.builder()
                .name("testName")
                .email("testmail@email.com")
                .password("1234")
                .build();

        RegisterRequest registerRequest2 = RegisterRequest.builder()
                .name("testName2")
                .email("testmail2@email.com")
                .password("12345")
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(registerRequest1))
                )
                .andDo(print())
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(registerRequest2))
                )
                .andDo(print())
                .andExpect(status().isCreated());

        TaskDtoRequest request = TaskDtoRequest.builder()
                .header("test header")
                .description("test description")
                .priority(Priority.HIGH.name())
                .status(Status.EXPECTATION.name())
                .taskAuthorId(1L)
                .taskPerformerId(2L)
                .build();

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(request))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    /**
     * 200
     */
    @Test
    void readById() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    /**
     * 404
     */
    @Test
    void readByIdAndGetNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/999")
                        //.content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }


    /**
     * 200
     */
    @Test
    void update() throws Exception {

        TaskDtoRequest request = TaskDtoRequest.builder()
                .header("test CHANGED")
                .description("test CHANGED")
                .priority(Priority.LOW.name())
                .status(Status.STARTED.name())
                .taskAuthorId(2L)
                .taskPerformerId(1L)
                .build();

        mockMvc.perform(patch("/api/v1/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(request))
                )
                .andDo(print())
                .andExpect(status().isOk());
                /*.andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.header", containsString("CHANGED")))
                .andExpect(jsonPath("$.description", containsString("CHANGED")))
                .andExpect(jsonPath("$.priority", is("LOW")))
                .andExpect(jsonPath("$.status", is("STARTED")))
                .andExpect(jsonPath("$.taskAuthorId", is(2)))
                .andExpect(jsonPath("$.taskPerformerId", is(1)));
                 */
    }

    /**
     * 204
     */
    @Test
    void deleteById() throws Exception {
        mockMvc.perform(delete("/api/v1/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    /**
     * 404
     */
    @Test
    void deleteByIdAndGetNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/tasks/999")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    private String json(Object o) throws IOException {
        return objectMapper.writeValueAsString(o);
    }
}