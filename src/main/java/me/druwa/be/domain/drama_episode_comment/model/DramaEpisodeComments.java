package me.druwa.be.domain.drama_episode_comment.model;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.druwa.be.domain.user.model.User;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class DramaEpisodeComments {

    @OneToMany
    private List<DramaEpisodeComment> dramaEpisodeComments;

    public List<DramaEpisodeComment.View.Read.Response> toResponse(final User user) {
        return dramaEpisodeComments.stream()
                                   .map(comment -> comment.toReadResponse(user))
                                   .collect(Collectors.toList());
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
