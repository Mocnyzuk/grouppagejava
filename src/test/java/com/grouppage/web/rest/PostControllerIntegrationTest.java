package com.grouppage.web.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.grouppage.domain.entity.Participant;
import com.grouppage.domain.entity.Post;
import com.grouppage.domain.notmapped.HashTag;
import com.grouppage.domain.repository.GroupRepository;
import com.grouppage.domain.repository.ParticipantRepository;
import com.grouppage.domain.repository.PostRepository;
import com.grouppage.domain.response.LoginRequest;
import com.grouppage.exception.ParticipantNotFountException;
import com.grouppage.exception.PostNotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostControllerIntegrationTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private GroupRepository groupRepository;

    public Post testPost;
    public Participant testParticipan;

    public static String accessTokenHeader;

    private static final ObjectMapper MAPPER = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @BeforeAll
    void startUp() throws Exception {
        MAPPER.registerModule(new JavaTimeModule());
        accessTokenHeader = this.authAsFpmoles();
        Participant participant = this.participantRepository.findAllByUserId(1).get(0);
        Post post = new Post();
        post.setReactionCount(5);
        post.setHashTags(Arrays.asList(new HashTag("#lul"), new HashTag("#heh")));
        post.setGroup(participant.getGroup());
        post.setAuthor(participant);
        post.setContent("testowy post ja piedziele");
        this.testPost = this.postRepository.save(post);
        this.testParticipan = participant;
    }

    @Test
    void voteUpBySelf() throws Exception {
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/post/upvote/" + this.testParticipan.getId())
                .header(HttpHeaders.AUTHORIZATION, accessTokenHeader)
                .param("id", String.valueOf(this.testPost.getId()))
        )
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();
        Post returnedPost = MAPPER.readValue(result.getResponse().getContentAsString(), Post.class);
        assertNotNull(returnedPost);
        assertEquals(testPost.getId(), returnedPost.getId());
        assertNotEquals(testPost.getReactionCount(), returnedPost.getReactionCount());
    }

    @Test
    void voteUpBySelfAndThenDevote() throws Exception{
        MvcResult upVote = this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/post/upvote/" + this.testParticipan.getId())
                .header(HttpHeaders.AUTHORIZATION, accessTokenHeader)
                .param("id", String.valueOf(this.testPost.getId()))
        )
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();
        Post returnedPost = MAPPER.readValue(upVote.getResponse().getContentAsString(), Post.class);
        assertNotNull(returnedPost);
        assertEquals(testPost.getId(), returnedPost.getId());
        //assertEquals(1, this.participantRepository.findById(this.testParticipan.getId()).get().getLikedPosts().size());
        assertNotEquals(testPost.getReactionCount(), returnedPost.getReactionCount());

        MvcResult removeVote = this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/post/removevote/" + this.testParticipan.getId())
                .header(HttpHeaders.AUTHORIZATION, accessTokenHeader)
                .param("id", String.valueOf(this.testPost.getId()))
        )
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();
        Post removedVotePost = MAPPER.readValue(removeVote.getResponse().getContentAsString(), Post.class);
        assertNotNull(returnedPost);
        assertEquals(testPost.getId(), returnedPost.getId());
        assertEquals(testPost.getReactionCount(), removedVotePost.getReactionCount());

    }


    @AfterAll
    void deleteInsertedData() {

        Participant parti = this.participantRepository.findById(this.testParticipan.getId()).get();
        parti.setLikedPosts(new ArrayList<>());
        this.participantRepository.save(parti);
        this.postRepository.delete(this.postRepository.findById(this.testPost.getId()).get());
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