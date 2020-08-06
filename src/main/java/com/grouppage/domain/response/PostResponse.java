package com.grouppage.domain.response;

import com.grouppage.domain.entity.Post;
import com.grouppage.domain.notmapped.GroupLight;
import com.grouppage.domain.notmapped.HashTag;
import com.grouppage.domain.notmapped.ParticipantLight;
import com.grouppage.domain.notmapped.PostLight;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private long id;

    private long group;

    private ParticipantLight author;

    private String content;

    private List<HashTag> hashTags;

    private int reactionCount;


    public static PostResponse fromPost(Post post, long groupId){
        return new PostResponse(
                post.getId(),
                groupId,
                ParticipantLight.fromParticipant(post.getAuthor()),
                post.getContent(),
                post.getHashTags(),
                post.getReactionCount()
        );
    }
}
