package com.grouppage.web.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grouppage.domain.entity.User;
import com.grouppage.domain.repository.UserRepository;
import com.grouppage.domain.response.LoginRequest;
import com.grouppage.domain.response.RegisterRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthControllerIntegrationTest {

    public static final String AUTH_HEADER = "Authorization";
    public static final String SET_COOKIE = "Set-Cookie";

    @Autowired
    private UserRepository userRepository;

    public static String fpmolesToken = "Bearer ";
    public static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    public MockMvc mockMvc;

    @BeforeEach
    void setup() throws Exception {
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                MAPPER.writeValueAsString(
                                        new LoginRequest(
                                                "fpmoles@fpmoles.pl",
                                                "password"
                                        ))
                        )
                )
                .andExpect(status().isAccepted())
                .andReturn();
        String accessCookie = result
                .getResponse()
                .getCookie("accessToken")
                .getValue();
        fpmolesToken = "Bearer ".concat(accessCookie);
    }

    @Test
    void loginWEmailIsNotEmail() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        MAPPER.writeValueAsString(
                                new LoginRequest(
                                        "wrongemailAdress",
                                        "password"
                                )
                        )
                ))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();
        assertNull(result.getResponse().getHeader(HttpHeaders.AUTHORIZATION));
        assertNull(result.getResponse().getHeader(HttpHeaders.SET_COOKIE));
    }

    @Test
    void registerNewUser() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        MAPPER.writeValueAsString(
                                new RegisterRequest(
                                        "jrczyson@jrczyson.pl",
                                        "123123123",
                                        "password",
                                        "password"
                                )
                        )
                ))
                .andExpect(status().isAccepted())
                .andDo(print())
                .andReturn();
        User user = userRepository.findByEmail("jrczyson@jrczyson.pl").orElseThrow(()->new UsernameNotFoundException("not found"));
        assertNotNull(user);
        assertEquals("123123123", user.getPhone());
        assertFalse(user.isActivated());
    }

    @Test
    void logOutFromFpMoles() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/logout")
                .header(HttpHeaders.AUTHORIZATION, fpmolesToken)
        )
                .andExpect(status().isNoContent())
                .andDo(print())
                .andReturn();
        assertEquals(0, result.getResponse().getCookie("accessToken").getMaxAge());
    }

    @AfterAll
    void cleanUp(){
        userRepository.delete(userRepository.findByEmail("jrczyson@jrczyson.pl").orElseThrow(
                () -> new UsernameNotFoundException("NOT FOUND")
        ));
    }

}