package com.grouppage.web.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grouppage.domain.response.LoginRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SocketChatControllerTest {

    static final String WEBSOCKET_URI = "ws://localhost:8080/websocketApp";
    static final String WEBSOCKET_TOPIC = "/topic";

    public static final String AUTH_HEADER = "Authorization";
    public static final String SET_COOKIE = "Set-Cookie";

    public static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    public MockMvc mockMvc;


    static BlockingQueue<String> blockingQueue;
    static WebSocketStompClient stompClient;
    static String fpmolesToken = "Bearer ";

    @BeforeEach
    void setup() throws Exception {
//        blockingQueue = new LinkedBlockingDeque<>();
//        stompClient = new WebSocketStompClient(new SockJsClient(
//                Arrays.asList(new WebSocketTransport(new StandardWebSocketClient()))));

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
        fpmolesToken = accessCookie.substring(accessCookie.indexOf("=")+1, accessCookie.indexOf(";"));
    }

    @Test
    void test1(){
        assertNotNull(fpmolesToken);
    }



}