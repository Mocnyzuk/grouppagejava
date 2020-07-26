package com.grouppage.web.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grouppage.domain.entity.Participant;
import com.grouppage.domain.entity.chat.PrivateMessage;
import com.grouppage.domain.notmapped.SocketMessage;
import com.grouppage.domain.response.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.AbstractNestablePropertyAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import javax.persistence.ManyToOne;
import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SocketControllerTest {

    public static final String WEBSOCKET_URI = "ws://localhost:{port}/websocketApp";
    public static final String WEBSOCKET_TOPIC = "/topic";

    @Autowired
    private MockMvc mockMvc;

    public static final ObjectMapper MAPPER = new ObjectMapper();

    @LocalServerPort
    private int port;

    private SockJsClient sockJsClient;

    private WebSocketStompClient stompClient;

    private final WebSocketHttpHeaders headers = new WebSocketHttpHeaders();


    @BeforeEach
    void setup() throws Exception {
        String accessToken = this.authAsFpmoles();
        headers.add(HttpHeaders.AUTHORIZATION, accessToken);
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        this.sockJsClient = new SockJsClient(transports);

        this.stompClient = new WebSocketStompClient(sockJsClient);
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }



    @Test
    void createNewConversationAndSendAMessageToListeningUser() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<Throwable> failure = new AtomicReference<>();




        StompSessionHandler handler = new TestSessionHandler(failure){
            @Override
            public void afterConnected(final StompSession session, StompHeaders connectedHeaders) {
                // Step 2: Simulate the client subscribing to a topic
                session.subscribe("/topic/1", new StompFrameHandler() {

                    @Override
                    public Type getPayloadType(StompHeaders headers) {

                        return SocketMessage.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        SocketMessage greeting = (SocketMessage) payload;
                        try {
                            int i = 10;
                            // Step 4:  Validate that the broadcast server response is correct
                            assertEquals("Pierwsza wiadomosc", greeting.getContent());
                        } catch (Throwable t) {
                            failure.set(t);
                        } finally {
                            session.disconnect();
                            latch.countDown();
                        }
                    }
                });
                try {
                    mockMvc.perform(MockMvcRequestBuilders.post("/api/message/new")
                            .headers(headers)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("receiver", "14")
                            .content(
                                    MAPPER.writeValueAsString(new SocketMessage(59, "Pierwsza wiadomosc", SocketMessage.Type.CHAT))
                            )
                    )
                            .andExpect(status().isCreated());
                    //session.send("/app/conversation/22/sendmessage", new SocketMessage(59, "Pierwsza wiadomosc", SocketMessage.Type.CHAT));
                } catch (Exception e) {
                    failure.set(e);
                    latch.countDown();
                }

//                try {
//                    // Step 3:  Simulate sending in a message from the client to the server
//                    SocketMessage myMessage = new SocketMessage(75, "Pierwsza wiadomosc", SocketMessage.Type.CHAT);
//                    session.send("/app/conversation/13/sendmessage", myMessage);
//                } catch (Throwable t) {
//                    failure.set(t);
//                    latch.countDown();
//                }
            }
        };
        this.stompClient.connect(
                WEBSOCKET_URI,
                this.headers,
                handler,
                this.port);


        if (latch.await(8, TimeUnit.SECONDS)) {
            if (failure.get() != null) {
                throw new AssertionError("", failure.get());
            }
        }
        else {
            fail("Greeting not received");
        }

    }


    private class TestSessionHandler extends StompSessionHandlerAdapter {

        private final AtomicReference<Throwable> failure;


        public TestSessionHandler(AtomicReference<Throwable> failure) {
            this.failure = failure;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            this.failure.set(new Exception(headers.toString()));
        }

        @Override
        public void handleException(StompSession s, StompCommand c, StompHeaders h, byte[] p, Throwable ex) {
            this.failure.set(ex);
        }

        @Override
        public void handleTransportError(StompSession session, Throwable ex) {
            this.failure.set(ex);
        }
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