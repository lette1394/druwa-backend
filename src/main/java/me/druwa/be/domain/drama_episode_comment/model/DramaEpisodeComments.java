package me.druwa.be.domain.drama_episode_comment.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.druwa.be.domain.user.model.User;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class DramaEpisodeComments {

    public DramaEpisodeComments(Collection<DramaEpisodeComment> comments) {
        this.dramaEpisodeComments = Sets.newHashSet(comments);
    }

    @OneToMany(mappedBy = "dramaEpisode")
    private Set<DramaEpisodeComment> dramaEpisodeComments;

    public List<DramaEpisodeComment.View.Read.Response> toResponse(final User user) {
        final List<DramaEpisodeComment.View.Read.Response> list = dramaEpisodeComments.stream()
                                                                                      .map(comment -> comment.toReadResponse(
                                                                                              user))
                                                                                      .sorted()
                                                                                      .collect(Collectors.toList());

        List<DramaEpisodeComment.View.Read.Response> temp = new ArrayList<>();

        for (final DramaEpisodeComment.View.Read.Response e : list) {
            if (e.getIsRoot()) {
                temp.add(e);

                List<DramaEpisodeComment.View.Read.Response> children = new ArrayList<>();

                for (final DramaEpisodeComment.View.Read.Response child : list) {
                    if (child.getIsRoot()) continue;

                    if (child.getPrev().equals(e.getId())) {
                        children.add(child);
                    }
                }
                temp.addAll(Lists.reverse(children));
            }
        }


        return temp;
    }

    public Integer count() {
        return dramaEpisodeComments.size();
    }

    @Data
    public static class View {
        @Data
        public static class Read {
            private List<DramaEpisodeComment.View.Read.Response> comments;
        }
    }
}
