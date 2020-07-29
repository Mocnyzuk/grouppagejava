package com.grouppage.domain.repository;

import com.grouppage.domain.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByName(String name);

}
