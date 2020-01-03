package me.druwa.be.domain.user.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.druwa.be.domain.common.model.Timestamp;

import static java.util.Objects.nonNull;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode(of = "userId")
@ToString
@AllArgsConstructor
@Table(name = "user_", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column
    @NotBlank
    private String name;

    @Column
    @NotBlank
    @Email
    private String email;

    @Column
    private String imageUrl;

    @Column
    private Boolean emailVerified = false;

    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private OAuth2Provider provider;

    @Column
    @NotBlank
    private String providerId;

    @Embedded
    @Column
    @NotNull
    private Authorities authorities;

    @Embedded
    @Column
    @NotNull
    private Timestamp timestamp;

    @PrePersist
    public void onCreate() {
        timestamp = new Timestamp();
        timestamp.onCreate();
    }

    @PreUpdate
    public void onUpdate() {
        if (nonNull(timestamp)) {
            timestamp.onUpdate();
        }
    }

    public List<GrantedAuthority> getGrantedAuthorities() {
        return authorities.toGrantedAuthorities();
    }
}
