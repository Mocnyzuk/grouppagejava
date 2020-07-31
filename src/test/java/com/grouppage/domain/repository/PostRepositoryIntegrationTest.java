package com.grouppage.domain.repository;

import com.grouppage.domain.entity.Group;
import com.grouppage.domain.entity.Participant;
import com.grouppage.domain.entity.Post;
import com.grouppage.domain.notmapped.HashTag;
import com.grouppage.exception.GroupNotFoundException;
import com.grouppage.exception.PostNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostRepositoryIntegrationTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Test
    void savingInDB(){
        Post group = new Post();
        group.setContent("asd");
        group.setReactionCount(3000);
        entityManager.persist(group);
        Post fromDB = this.postRepository.findByReactionCount(3000).orElseThrow(
                () -> new PostNotFoundException("NOT FOUND")
        );
        assertEquals(group.getId(), fromDB.getId());
        assertEquals(group.getContent(), fromDB.getContent());
        assertEquals(group.getReactionCount(), fromDB.getReactionCount());
    }

    @Test
    void testForLatestPostFromGroups(){
//        List<Post> allPosts = this.postRepository.findAll();
        List<Post> fetchedAll = this.postRepository.fetchAllProperties();

//        Map<Group, List<Post>> map = fetchedAll
//                .stream()
//                .collect(groupingBy(Post::getGroup));

        List<Participant> participants = this.participantRepository.findAllByUserIdFetchGroup(1);
        List<Group> groups = participants
                .stream()
                .map(Participant::getGroup)
                .filter(Group.distinctByKeys(Group::getId))
                .collect(Collectors.toList());

        List<Post> posts = this.postRepository.findLatestPostFromGroups(groups);

        assertEquals(fetchedAll.size(), posts.size());
    }
}