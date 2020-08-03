package com.grouppage.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.io.Serializable;
import java.time.Instant;

@Data
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
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

}
