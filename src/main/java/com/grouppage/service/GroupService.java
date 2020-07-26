package com.grouppage.service;

import com.grouppage.domain.entity.Group;
import com.grouppage.domain.entity.Post;
import com.grouppage.domain.repository.GroupRepository;
import com.grouppage.domain.repository.ParticipantRepository;
import com.grouppage.domain.repository.PostRepository;
import com.grouppage.domain.response.GroupSearch;
import com.grouppage.domain.response.PostedPost;
import com.grouppage.exception.GroupNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final PostRepository postRepository;
    private final ParticipantRepository participantRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository, PostRepository postRepository, ParticipantRepository participantRepository) {
        this.groupRepository = groupRepository;
        this.postRepository = postRepository;
        this.participantRepository = participantRepository;
    }

    public void handleNewPost(PostedPost postedPost, Group group){

    }

    public List<Post> getPostForGroupId(Group group)throws GroupNotFoundException {
        long groupId = group.getId();
        return postRepository.findAllByGroup(
                groupRepository.findById(groupId).orElseThrow(
                        () -> new GroupNotFoundException("Group with id: "+groupId+" doesnt exists!")
                )
        );
    }

    public List<GroupSearch> findGroupBySearchPhrase(String phrase) {
        return null;
    }
}
