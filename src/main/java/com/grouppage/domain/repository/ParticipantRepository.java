package com.grouppage.domain.repository;

import com.grouppage.domain.entity.Group;
import com.grouppage.domain.entity.Participant;
import com.grouppage.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    List<Participant> findAllByUser(User user);

    @Query(value = "select p from Participant p join fetch p.group g where p.user = :user")
    List<Participant> findAllByUserFetchGroup(@Param("user") User user);

    @Query(value = "select p from Participant  p join fetch p.group g where p.user.id = :id ")
    List<Participant> findAllByUserIdFetchGroup(@Param("id") long id);

    List<Participant> findAllByUserId(long id);

    List<Participant> findAllByGroupIn(Iterable<Group> groups);

    List<Participant> findAllByGroupId(long id);

    @Query(value = "select p from Participant p join fetch p.user u where p.id = :id")
    Optional<Participant> findByIdFetchUser(@Param("id") long id);

    @Query(value = "select p from Participant p join fetch p.user u join fetch p.group g where p.id = :id")
    Optional<Participant> findByIdFetchUserFetchGroup(@Param("id")long id);

    @Query(value = "select p from Participant p join fetch p.group g where g.id = :id")
    List<Participant> findAllByGroupIdFetchGroup(@Param("id") long id);

    Optional<Participant> findByNickname(String nickname);

    @Query(value = "select p from Participant p join fetch p.group g where g.id = :groupId and p.user.id = :userId")
    Optional<Participant> findByUserIdAndGroupIdFetchGroup(@Param("groupId")long groupId, @Param("userId")long userId);

    @Query(value = "select p from Participant p where p.group.id = :groupId and p.user.id = :userId")
    Optional<Participant> findByUserIdAndGroupId(@Param("groupId")long groupId, @Param("userId")long userId);

    @Query(value = "select p from Participant p where p.nickname in :nicknames and p.group.id = :groupId")
    List<Participant> findAllByNicknameAndGroupId(@Param("nicknames")String[] nicknames, long groupId);
}
