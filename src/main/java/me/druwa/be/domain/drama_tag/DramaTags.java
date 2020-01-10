package me.druwa.be.domain.drama_tag;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import me.druwa.be.domain.common.model.Mergeable;

@EqualsAndHashCode
@RequiredArgsConstructor
public class DramaTags implements Mergeable<DramaTags> {
    private final Set<DramaTag> dramaTags;

    public static DramaTags dramaTags(final Collection<DramaTag> dramaTags) {
        return new DramaTags(Sets.newHashSet(dramaTags));
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
        dramaTags.addAll(other.dramaTags);
        return this;
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
}
