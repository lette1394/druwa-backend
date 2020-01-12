package me.druwa.be.domain.drama_episode_comment.model;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class DramaEpisodeComments {

    @OneToMany
    private List<DramaEpisodeComment> dramaEpisodeComments;

    public List<DramaEpisodeComment.View.Read.Response> toResponse() {
        return dramaEpisodeComments.stream()
                                   .map(DramaEpisodeComment::toReadResponse)
                                   .collect(Collectors.toList());
    }

    @Data
    public static class View {
        @Data
        public static class Read {
            private List<DramaEpisodeComment.View.Read.Response> comments;
        }
    }
}
