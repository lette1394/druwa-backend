package me.druwa.be.domain.common.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Embeddable
public class Timestamp {

    @Column
    @NotNull
    private LocalDateTime createdAt;

    @Column
    @NotNull
    private LocalDateTime updatedAt;

    public Timestamp() {
        onCreate();
    }

    public static Timestamp now() {
        return new Timestamp();
    }

    @PrePersist
    public void onCreate() {
        updatedAt = createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
