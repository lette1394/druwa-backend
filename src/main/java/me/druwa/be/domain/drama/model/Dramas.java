package me.druwa.be.domain.drama.model;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.ManyToMany;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Dramas {

    @ManyToMany
    private Set<Drama> dramas;

    public static Dramas dramas(final Collection<Drama> dramas) {
        return new Dramas(Sets.newHashSet(dramas));
    }

    public Dramas.View.Search.Response toSearchResponse() {
        final Set<Drama.View.Search.Response> responses = dramas.stream()
                                                                .map(Drama::toSearchResponse)
                                                                .collect(Collectors.toSet());
        return View.Search.Response.builder()
                                   .dramas(responses)
                                   .total(responses.size())
                                   .build();
    }

    public static class View {
        public static class Search {
            @Data
            @Builder
            public static class Response {
                private Set<Drama.View.Search.Response> dramas;

                private Integer total;
            }
        }
    }
}
