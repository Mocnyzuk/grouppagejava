package com.grouppage.domain.repository;

import com.grouppage.domain.entity.SignUpForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SignUpFormRepository extends JpaRepository<SignUpForm, Long> {

    @Query("select s from SignUpForm s where s.group.id = :groupId and s.nickname = 'example'")
    Optional<SignUpForm> findByGroupId(@Param("groupId") long groupId);
}
