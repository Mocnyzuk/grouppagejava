package com.grouppage.domain.repository;

import com.grouppage.domain.entity.SignUpForm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignUpFormRepository extends JpaRepository<SignUpForm, Long> {
}
