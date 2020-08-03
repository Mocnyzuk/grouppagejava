package com.grouppage.domain.repository;

import com.grouppage.domain.entity.Group;
import com.grouppage.domain.entity.Participant;
import com.grouppage.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    List<Participant> findAllByUser(User user);

    @Query(value = "select p from Participant p join fetch p.group g where p.user = :user")
    List<Participant> findAllByUserFetchGroup(@Param("user") User user);

    @Query(value = "select p from Participant  p join fetch p.group g where p.user.id = :id ")
    List<Participant> findAllByUserIdFetchGroup(@Param("id") long id);

    List<Participant> findAllByUserId(long id);

    List<Participant> findAllByGroupIn(Iterable<Group> groups);

    List<Participant> findAllByGroupId(long id);
}
