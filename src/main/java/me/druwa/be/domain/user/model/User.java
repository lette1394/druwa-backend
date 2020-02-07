package me.druwa.be.domain.user.model;

import java.time.LocalDateTime;
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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.druwa.be.domain.auth.service.TokenProvider;
import me.druwa.be.domain.common.model.Timestamp;
import me.druwa.be.domain.drama_review.DramaReviews;

import static java.util.Objects.nonNull;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode(of = "userId")
@ToString(of = { "userId", "email", "name", "provider" })
@AllArgsConstructor
@Table(name = "user_", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@Builder
public class User {
    private static final int MIN_NAME_SIZE = 2;
    private static final int MAX_NAME_SIZE = 20;

    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "user_id")
    private Long userId;

    @Column
    @NotBlank
    @Size(min = MIN_NAME_SIZE, max = MAX_NAME_SIZE)
    private String name;

    @Column
    @NotBlank
    @Email
    private String email;

    @Column
    private String password;

    @Column
    private String imageUrl;

    @Column
    @Builder.Default
    private Boolean emailVerified = false;

    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private OAuth2Provider provider;

    @Column
    private String providerId;

    @Embedded
    @Column
    @NotNull
    private Authorities authorities;

    @Embedded
    @Column
    @NotNull
    private Timestamp timestamp;

    @Embedded
    private DramaReviews dramaReviews;

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

    public View.Create.Response toCreateResponse(final TokenProvider tokenProvider) {
        return View.Create.Response.builder()
                                   .token(tokenProvider.createToken(this))
                                   .build();
    }

    public View.Read.Response toReadResponse() {
        return View.Read.Response.builder()
                                 .name(name)
                                 .email(email)
                                 .imageUrl(imageUrl)
                                 .provider(provider.name())
                                 .registeredAt(timestamp.getCreatedAt())
                                 .isEmailVerified(emailVerified)
                                 .build();
    }

    public boolean isMatchEmailAndPassword(final User user) {
        return this.email.equals(user.email) && this.password.equals(user.password);
    }

    public static class View {
        public static class Create {
            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Request {
                @Email
                @NotBlank
                private String email;

                @NotBlank
                @Size(min = MIN_NAME_SIZE, max = MAX_NAME_SIZE)
                private String name;

                @NotBlank
                @Pattern(regexp = PASSWORD_REGEX)
                private String password;

                public User toPartialUser(final PasswordEncoder passwordEncoder) {
                    return User.builder()
                               .email(email)
                               .name(name)
                               .password(passwordEncoder.encode(password))
                               .authorities(Authorities.user())
                               .provider(OAuth2Provider.LOCAL)
                               .build();
                }
            }

            @Data
            @Builder
            public static class Response {
                @NotBlank
                private String token;
            }
        }

        public static class Read {
            @Data
            @Builder
            public static class Response {
                @NotBlank
                private String name;
                @NotBlank
                private String email;
                private String imageUrl;
                private String provider;
                @NotNull
                private LocalDateTime registeredAt;
                @NotNull
                private Boolean isEmailVerified;
            }
        }

        public static class Find {
            @Data
            public static class Request {
                @NotBlank
                @Size(min = MIN_NAME_SIZE, max = MAX_NAME_SIZE)
                private String name;

                @Email
                @NotBlank
                private String email;
            }
        }

        public static class Login {
            @Data
            public static class Request {
                @Email
                @NotBlank
                private String email;

                @NotBlank
                @Pattern(regexp = PASSWORD_REGEX)
                private String password;

                public User toPartialUser(final PasswordEncoder passwordEncoder) {
                    return User.builder()
                               .email(email)
                               .password(passwordEncoder.encode(password))
                               .build();
                }
            }
        }
    }
}
