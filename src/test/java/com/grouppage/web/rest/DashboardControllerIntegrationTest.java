package com.grouppage.web.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.grouppage.domain.entity.Group;
import com.grouppage.domain.entity.Participant;
import com.grouppage.domain.entity.Post;
import com.grouppage.domain.notmapped.GroupLight;
import com.grouppage.domain.notmapped.HashTag;
import com.grouppage.domain.notmapped.PostLight;
import com.grouppage.domain.repository.GroupRepository;
import com.grouppage.domain.repository.ParticipantRepository;
import com.grouppage.domain.repository.PostRepository;
import com.grouppage.domain.response.DashboardResponse;
import com.grouppage.domain.response.LoginRequest;
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

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DashboardControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    public static String accessTokenHeader;

    private static final ObjectMapper MAPPER = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @Test
    void userShouldGet5LatestPostsFromAllGroupsHeParticipateIn() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/api")
                .header(HttpHeaders.AUTHORIZATION, accessTokenHeader)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        List<Participant> participants = this.participantRepository.findAllByUserIdFetchGroup(1);
        List<Group> groups = participants.stream()
                .map(Participant::getGroup)
                .filter(Group.distinctByKeys(Group::getId))
                .sorted(Comparator.comparing(Group::getId))
                .collect(Collectors.toList());
        List<Post> posts = this.postRepository.findLatestPostFromGroups(groups);
        Map<GroupLight, List<Post>> map = posts.stream()
                .collect(Collectors.groupingBy(g -> GroupLight.fromGroup(g.getGroup())));
        Set<GroupLight> keySet = map.keySet();
        GroupLight gl = null;
        for (GroupLight groupLight : keySet) {
            if(groupLight.getId() == 1) {
                gl = groupLight;
                break;
            }
        }

        DashboardResponse[] response = MAPPER.readValue(result.getResponse().getContentAsString(), DashboardResponse[].class);
        List<DashboardResponse> responseList = Arrays.stream(response).sorted(Comparator.comparing(d -> d.getGroup().getId())).collect(Collectors.toList());
        List<PostLight> array = map.get(gl).stream().sorted(Comparator.comparing(Post::getCreatedDate).reversed()).limit(5).map(PostLight::fromPost).collect(Collectors.toList());
        assertArrayEquals(array.toArray(), responseList.get(0).getPosts().toArray());
        assertEquals(groups.stream().sequential().map(GroupLight::fromGroup).toArray()[0], response[0].getGroup());


    }


    @BeforeAll
    void startUp() throws Exception {
        MAPPER.registerModule(new JavaTimeModule());
        accessTokenHeader = this.authAsFpmoles();

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