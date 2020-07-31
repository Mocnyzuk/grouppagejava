package com.grouppage.domain.repository;

import com.grouppage.domain.entity.Group;
import com.grouppage.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByGroup(Group group);

    Page<Post> findAllByGroupId(long group_id, Pageable pageable);

    Page<Post> findAllByGroup(Group group, Pageable pageable);


    @Query(value = "select p from Post p left join fetch p.author left join fetch p.group g where g in :groups")
    List<Post> findLatestPostFromGroups(List<Group> groups);


}
