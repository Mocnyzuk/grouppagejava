package com.grouppage.domain.repository;

import com.grouppage.domain.entity.SignUpForm;
import com.grouppage.domain.notmapped.SignUpFormLight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SignUpFormRepository extends JpaRepository<SignUpForm, Long> {

    @Query("select s from SignUpForm s where s.group.id = :groupId and s.nickname = 'example'")
    Optional<SignUpForm> findByGroupId(@Param("groupId") long groupId);

    @Query(value = "select s from SignUpForm s join fetch s.group g where g.id = :groupId and s.nickname <> 'example' and s.checked = false")
    List<SignUpForm> findAllByGroupIdFetchGroup(@Param("groupId")long groupId);
}
