package me.druwa.be.domain.comment.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Comments {
    @Column
    @NotNull
    @OneToMany
    @JoinColumn
    private List<Comment> comments = new ArrayList<>();
}
