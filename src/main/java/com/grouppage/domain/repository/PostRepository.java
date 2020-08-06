package com.grouppage.domain.repository;

import com.grouppage.domain.entity.Group;
import com.grouppage.domain.entity.Post;
import org.hibernate.annotations.Parent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByGroup(Group group);

    Page<Post> findAllByGroupId(long groupId, Pageable pageable);

    @Query("select p from Post p join fetch p.author a where p.group.id = :groupId")
    List<Post> findAllByGroupIdFetchAuthor(@Param("groupId") long groupId);

    Page<Post> findAllByGroup(Group group, Pageable pageable);

    Optional<Post> findByReactionCount(int reactionCount);

    @Query(value = "select p from Post p left join fetch p.author left join fetch p.group g where g in :groups")
    List<Post> findLatestPostFromGroups(@Param("groups") List<Group> groups);

    @Query("select p from Post p fetch all properties")
    List<Post> fetchAllProperties();


    @Query("select p from Post p join fetch p.author a join fetch p.group g where g.id = :groupId")
    List<Post> findAllByGroupIdFetchAuthorAndGroup(@Param("groupId") long groupId);


}
