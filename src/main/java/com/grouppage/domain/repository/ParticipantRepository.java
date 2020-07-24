package com.grouppage.domain.repository;

import com.grouppage.domain.entity.Group;
import com.grouppage.domain.entity.Participant;
import com.grouppage.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    List<Participant> findAllByUser(User user);

    List<Participant> findAllByUserId(long id);

    List<Participant> findAllByGroupIn(Iterable<Group> groups);
}
