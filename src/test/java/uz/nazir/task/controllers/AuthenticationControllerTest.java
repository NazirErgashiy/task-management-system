package uz.nazir.task.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import uz.nazir.task.IntegrationTestInitializer;
import uz.nazir.task.dto.security.request.AuthenticationRequest;
import uz.nazir.task.dto.security.request.RegisterRequest;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@Order(1)
@ContextConfiguration(initializers = IntegrationTestInitializer.class)
@SpringBootTest
@WebAppConfiguration
class AuthenticationControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext)
                .build();
    }

    /**
     * 201
     */
    @Test
    void registerAndExpectStatusCreated() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .name("test")
                .email("test@email.com")
                .password("test")
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token", notNullValue()));
    }

    /**
     * 400
     */
    @Test
    void registerAndExpectStatusBadRequest1() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .name("t")
                .email("test@email.com")
                .password("test")
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(request))
                )
                .andExpect(status().isBadRequest());
    }

    /**
     * 400
     */
    @Test
    void registerAndExpectStatusBadRequest2() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .name("test")
                .email("testeom")
                .password("test")
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(request))
                )
                .andExpect(status().isBadRequest());
    }

    /**
     * 400
     */
    @Test
    void registerAndExpectStatusBadRequest3() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .name("test")
                .email("test@email.com")
                .password("te")
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(request))
                )
                .andExpect(status().isBadRequest());
    }

    /****************************************************************
     * 200
     */
    @Test
    void authenticateAndExpectStatusOK() throws Exception {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("test@email.com")
                .password("test")
                .build();

        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()));
    }

    /**
     * 400
     */
    @Test
    void authenticateAndExpectStatusBadRequest1() throws Exception {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("tesil.com")
                .password("test")
                .build();

        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(request))
                )
                .andExpect(status().isBadRequest());
    }

    /**
     * 401
     */
    @Test
    void authenticateAndExpectUnauthorized() throws Exception {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("test@email.com")
                .password("testtest")
                .build();

        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(request))
                )
                .andExpect(status().isUnauthorized());
    }

    private String json(Object o) throws IOException {
        return objectMapper.writeValueAsString(o);
    }
}