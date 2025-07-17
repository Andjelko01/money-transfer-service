package com.andjelko.moneytransferservice.datasource.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Getter
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "modified_at", nullable = false)
    private OffsetDateTime modifiedAt = OffsetDateTime.now();

    @PrePersist
    protected void prePersist() {
        this.createdAt = OffsetDateTime.now();
    }

    @PreUpdate
    protected void preUpdate() {
        this.modifiedAt = OffsetDateTime.now();
    }
}
