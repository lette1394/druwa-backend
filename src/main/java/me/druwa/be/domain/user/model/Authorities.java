package me.druwa.be.domain.user.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static java.lang.String.format;

@Embeddable
@ToString
@EqualsAndHashCode
public class Authorities {
    @NotEmpty
    private String authorities;

    public static Authorities user(Authority... authorities) {
        return of(Authority.USER);
    }

    public static Authorities of(Authority... authorities) {
        final Authorities instance = new Authorities();
        instance.authorities = Arrays.stream(authorities)
                                     .map(Enum::toString)
                                     .collect(Collectors.joining(","));
        return instance;
    }

    List<GrantedAuthority> toGrantedAuthorities() {
        return Arrays.stream(authorities.split(","))
                     .map(auth -> format("ROLE_%s", auth))
                     .map(SimpleGrantedAuthority::new)
                     .collect(Collectors.toList());
    }
}
