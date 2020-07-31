package com.grouppage.domain.notmapped;

import com.grouppage.domain.entity.Participant;
import com.grouppage.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostLight {
    private long id;
    private ParticipantLight author;
    private String content;
    private List<HashTag> hashTags;
    private int reactionCount;

    public static PostLight fromPost(Post post){
        return new PostLight(post.getId(),
                ParticipantLight.fromParticipant(post.getAuthor()),
                post.getContent(), post.getHashTags(),
                post.getReactionCount());
    }
}
