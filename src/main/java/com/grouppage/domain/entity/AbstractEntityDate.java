package com.grouppage.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.criteria.CriteriaBuilder;
import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
public class AbstractEntityDate implements Serializable {
    private static final long serialVersionUID = 1L;

    @CreatedDate
    @Column(updatable = false)
    private Instant createdDate;

    @LastModifiedDate
    private Instant lastModifiedDate;

    @PrePersist
    public void prePersist(){
        this.createdDate = Instant.now();
        this.lastModifiedDate = Instant.now();
    }

    @PreUpdate
    public void preUpdate(){
        this.lastModifiedDate = Instant.now();
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
