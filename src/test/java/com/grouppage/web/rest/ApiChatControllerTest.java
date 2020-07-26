package com.grouppage.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grouppage.domain.entity.chat.Conversation;
import com.grouppage.domain.repository.chat.ConversationRepository;
import com.grouppage.domain.response.AddParticipantRequest;
import com.grouppage.domain.response.LoginRequest;
import com.grouppage.exception.ConversationNotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApiChatControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ConversationRepository conversationRepository;

    private String accessTokenHeader;

    private final String URL = "/api/message/add";

    public static final ObjectMapper MAPPER = new ObjectMapper();


    @BeforeEach
    void setup() throws Exception {
        this.accessTokenHeader = this.authAsFpmoles();
    }

    @Test
    void addParticipantToExistingConversation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post(URL)
                .header(HttpHeaders.AUTHORIZATION, this.accessTokenHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        MAPPER.writeValueAsString(
                                new AddParticipantRequest(1, 37)
                        )))
                .andExpect(status().isCreated())
                .andDo(print());

        assertTrue(conversationRepository.findById(1L).orElseThrow(
                () -> new ConversationNotFoundException("not foud")
        ).getParticipants().stream().anyMatch(p -> p.getId() == 37));
    }
    @AfterAll
    void deleteInsertionFromTests(){
        Conversation conv = conversationRepository.findById(1L).orElseThrow(
                () -> new ConversationNotFoundException("not found")
        );
        conv.setParticipants(conv.getParticipants().stream().filter(p -> p.getId() != 37).collect(Collectors.toList()));
        conversationRepository.save(conv);
    }

    private String authAsFpmoles() throws Exception {
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
        return "Bearer ".concat(accessCookie);
    }
}