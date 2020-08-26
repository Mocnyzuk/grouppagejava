package com.grouppage.domain.repository;

import com.grouppage.domain.entity.Group;
import com.grouppage.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByName(String name);

    @Query(value = "select g from Group g where (g.isPrivate = false) and (( g.category like %:param% )  " +
            "or (g.description like %:param%) or (g.name like %:param%))")
    Page<Group> proceedGroupSearch(@Param("param") String param, Pageable pageable);

    @Query(value = "select g from Group g where (g.isPrivate = false) and ((g.category like %:param% )" +
            "or (g.description like %:param%) or (g.name like %:param%))")
    List<Group> proceedGroupSearch(@Param("param") String param);

    Optional<Group> findByInviteCode(String inviteCode);


}
