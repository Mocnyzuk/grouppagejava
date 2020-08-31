package com.grouppage.web.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.grouppage.domain.entity.Group;
import com.grouppage.domain.entity.Participant;
import com.grouppage.domain.entity.Post;
import com.grouppage.domain.notmapped.GroupForm;
import com.grouppage.domain.notmapped.GroupLight;
import com.grouppage.domain.repository.GroupRepository;
import com.grouppage.domain.repository.ParticipantRepository;
import com.grouppage.domain.repository.PostRepository;
import com.grouppage.domain.response.*;
import com.grouppage.exception.GroupNotFoundException;
import com.grouppage.exception.ParticipantNotFountException;
import com.grouppage.service.json.PageModule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.PostRemove;
import javax.print.attribute.standard.Media;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GroupControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private PostRepository postRepository;

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

    @BeforeAll
    void startUp() throws Exception {
        MAPPER.registerModule(new JavaTimeModule());
        MAPPER.registerModule(new PageModule());
        accessTokenHeader = this.authAsFpmoles();

    }

    @Test
    void shouldReturnGroupsBasedOnSearchPhrase() throws Exception {
        String phrase = "IT";
        List<GroupLight> all = this.groupRepository.findAll().stream()
                .map(GroupLight::fromGroup).collect(Collectors.toList());
        List<GroupLight> expected = all.stream()
                .filter(g -> g.getName().contains(phrase) ||
                        g.getDescription().contains(phrase) ||
                        g.getCategory().contains(phrase))
                .collect(Collectors.toList());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/group")
                .header(HttpHeaders.AUTHORIZATION, accessTokenHeader)
                .param("search", phrase)
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        Page<GroupLight> page = MAPPER.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<Page<GroupLight>>() {}
        );
        assertNotNull(page);
        assertEquals(expected.size(), page.toList().size());
        assertArrayEquals(expected.toArray(), page.toList().toArray());
    }
    @Test
    void shouldReturnNoGroupsBasedOnSearchPhrase() throws Exception {
        String phrase = "asdas87d6as9d8";
        List<GroupLight> all = this.groupRepository.findAll().stream()
                .map(GroupLight::fromGroup).collect(Collectors.toList());
        List<GroupLight> expected = all.stream()
                .filter(g -> g.getName().contains(phrase) ||
                        g.getDescription().contains(phrase) ||
                        g.getCategory().contains(phrase))
                .collect(Collectors.toList());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/group")
                .header(HttpHeaders.AUTHORIZATION, accessTokenHeader)
                .param("search", phrase)
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        Page<GroupLight> page = MAPPER.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<Page<GroupLight>>() {}
        );
        assertNotNull(page);
        assertEquals(expected.size(), page.toList().size());
        assertEquals(0, page.toList().size());
        assertArrayEquals(expected.toArray(), page.toList().toArray());
    }

    @Test
    void shouldReturnXNumberOfLatestPostForGroup() throws Exception {
        Group group = this.groupRepository.findById(1L).orElseThrow(
                () -> new GroupNotFoundException("NOT FOUND")
        );
        List<Post> posts = this.postRepository.findAllByGroupIdFetchAuthor(group.getId());
        List<PostResponse> responsesFromDB = posts.stream()
                .map(p -> PostResponse.fromPost(p, 1L))
                .collect(Collectors.toList());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/group/"+group.getId())
                .header(HttpHeaders.AUTHORIZATION, accessTokenHeader)
                .param("size", "4")
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        Page<PostResponse> page = MAPPER.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<Page<PostResponse>>() {}
        );
        assertNotNull(page);
        assertTrue(responsesFromDB.containsAll(page.toList()));
        assertEquals(4, page.toList().size());
        assertEquals((posts.size()/4) + 1, page.getTotalPages());
        assertTrue(posts.size() >= page.toList().size());
    }

    @Test
    void shouldSaveNewPostForGroup() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/group")
                .header(HttpHeaders.AUTHORIZATION, accessTokenHeader)
                .param("group", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        MAPPER.writeValueAsString(
                                new PostedPost(
                                        1L,
                                        1L,
                                        "nowy post do handlera"
                                )
                        )
                )
        )
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();
        Thread.sleep(1000);
        List<Post> posts = this.postRepository.findAllByGroupIdFetchAuthorAndGroup(1L);
        assertTrue(posts.stream().anyMatch(p -> p.getContent().equals("nowy post do handlera")));
        assertEquals(1,
                posts.stream()
                        .filter(p ->
                                p.getContent().equals("nowy post do handlera") &&
                                p.getGroup().getId() == 1L &&
                                p.getAuthor().getId() == 1L).count()
        );
    }

    @Test
    void shouldSaveNewGroupWithSignUpForm() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("lala", "lala");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/group/new")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, accessTokenHeader)
                .content(
                        MAPPER.writeValueAsString(
                                new RequestNewGroup(
                                        "administratoros",
                                        "groupaadministratorosa",
                                        "groupdescofadminisratosoa",
                                        "#AADDEE",
                                        "caegory of this new group",
                                        "iamge",
                                        false,
                                        false,
                                        new GroupForm(map),
                                        1
                                )
                        )
                )
        )
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();
    }

    @Test
    void shouldReturnGroupLightForValidInviteCode() throws Exception {
        Group group = this.groupRepository.findById(17L).orElseThrow(
                () -> new GroupNotFoundException("nie ma")
        );
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/group/invite")
                .param("id", group.getInviteCode())
                .header(HttpHeaders.AUTHORIZATION, accessTokenHeader)
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        GroupLight gl = MAPPER.readValue(result.getResponse().getContentAsString(), GroupLight.class);
        assertEquals(group.getId(), gl.getId());
        assertEquals(group.getName(), gl.getName());
        assertEquals(group.getInviteCode(), gl.getInviteCode());

    }

    @Test
    void shouldAddNewParticipantForGroupByInviteCode() throws Exception {
        Group group = this.groupRepository.findById(17L).orElseThrow(
                () -> new GroupNotFoundException("nie ma")
        );
        Map<String, String> map = new HashMap<>();
        map.put("lala", "lala");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/group/invite/participant")
                .param("id", group.getInviteCode())
                .header(HttpHeaders.AUTHORIZATION, accessTokenHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        MAPPER.writeValueAsString(
                                new InviteParticipant(
                                        new GroupForm(map)
                                )
                        )
                )
        )
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();
        Thread.sleep(1000); //w8 till async task of saving ends
        List<Participant> participants = this.participantRepository.findAllByGroupIdFetchGroup(group.getId());
        assertTrue(participants.stream().anyMatch(p -> (p.getGroup().getId() == group.getId()) && (p.getNickname().equals("jajajajsdadjsadajsd"))));
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

    @AfterAll
    void afterAll(){
        this.participantRepository.delete(this.participantRepository.findByNickname("jajajajsdadjsadajsd").orElseThrow(
                () -> new ParticipantNotFountException("NOT FOUND")
        ));
    }

}