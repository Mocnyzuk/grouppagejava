package com.grouppage.web.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grouppage.domain.response.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
class AuthControllerTest {

    public static final String AUTH_HEADER = "Authorization";
    public static final String SET_COOKIE = "Set-Cookie";

    static String fpmolesToken = "Bearer ";
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
                .andDo(print())
                .andReturn();
        String accessCookie = result
                .getResponse()
                .getHeader(SET_COOKIE);
        fpmolesToken = fpmolesToken.concat(accessCookie.substring(accessCookie.indexOf("=")+1, accessCookie.indexOf(";")));
    }
}