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
import uz.nazir.task.dto.request.UserDtoRequest;
import uz.nazir.task.dto.security.request.RegisterRequest;
import uz.nazir.task.entities.enums.Role;
import uz.nazir.task.validators.RoleValidator;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@Order(3)
@ContextConfiguration(initializers = IntegrationTestInitializer.class)
@SpringBootTest
@WebAppConfiguration
class UserControllerTest {

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
        doThrow(new RuntimeException()).when(validator).checkSelfEditing(any(), any());
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
        mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * 201
     */
    @Test
    void create() throws Exception {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .name("testNametst")
                .email("testmailtst@email.com")
                .password("123456789")
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(registerRequest))
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    /**
     * 200
     */
    @Test
    void readById() throws Exception {
        mockMvc.perform(get("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * 404
     */
    @Test
    void readByIdAndGetNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * 200
     */
    @Test
    void update() throws Exception {
        UserDtoRequest request = UserDtoRequest.builder()
                .role(Role.USER.name())
                .name("testName")
                .email("testemail@email.com")
                .build();

        mockMvc.perform(patch("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(request))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * 404
     */
    @Test
    void updateAndGetNotFound() throws Exception {
        UserDtoRequest request = UserDtoRequest.builder()
                .role(Role.USER.name())
                .name("testName")
                .email("testemail@email.com")
                .build();

        mockMvc.perform(patch("/api/v1/users/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(request))
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    /**
     * 204
     */
    @Test
    void deleteById() throws Exception {
        mockMvc.perform(delete("/api/v1/users/1")
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
        mockMvc.perform(delete("/api/v1/users/999")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    private String json(Object o) throws IOException {
        return objectMapper.writeValueAsString(o);
    }
}