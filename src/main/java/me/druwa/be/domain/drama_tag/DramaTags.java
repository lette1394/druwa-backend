package me.druwa.be.domain.drama_tag;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.ManyToMany;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.druwa.be.domain.common.model.Mergeable;

@Embeddable
@EqualsAndHashCode(of = "dramaTags")
@ToString(of = "dramaTags")
@NoArgsConstructor
@AllArgsConstructor
public class DramaTags implements Mergeable<DramaTags> {

    @ManyToMany
    private Set<DramaTag> dramaTags;

    public static DramaTags dramaTags(final Collection<DramaTag> dramaTags) {
        return new DramaTags(Sets.newHashSet(dramaTags));
    }

    public static DramaTags dramaTags(final String... str) {
        return DramaTags.dramaTags(Sets.newHashSet(str)
                                       .stream()
                                       .map(DramaTag::new)
                                       .collect(Collectors.toSet()));
    }

    public DramaTags(final List<DramaTag> dramaTags) {
        this.dramaTags = Sets.newHashSet(dramaTags);
    }

    public View.Response toResponse() {
        return new View.Response(this.dramaTags.stream()
                                               .map(DramaTag::raw)
                                               .collect(Collectors.toSet()));
    }

    @Override
    public DramaTags merge(final DramaTags other) {
        return dramaTags(dramaTags).append(other);
    }

    public DramaTags filter(final DramaTags other) {
        return dramaTags(other.dramaTags.stream()
                                        .filter(other::have)
                                        .collect(Collectors.toSet()));
    }

    private DramaTags append(final DramaTags other) {
        this.dramaTags.addAll(other.dramaTags);
        return this;
    }

    private boolean have(final DramaTag tag) {
        return dramaTags.contains(tag);
    }

    private boolean notHave(final DramaTag tag) {
        return !have(tag);
    }

    public void update(final DramaTags other) {
        dramaTags = other.dramaTags;
    }

    public static class View {

        @Data
        @AllArgsConstructor
        public static class Response {
            private Set<String> tags;
        }
    }

    public Set<DramaTag> raw() {
        return dramaTags;
    }

    public Set<String> rawNames() {
        return dramaTags.stream()
                        .map(DramaTag::raw)
                        .collect(Collectors.toSet());
    }
}
