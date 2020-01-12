package me.druwa.be.domain.drama.model;

import java.util.Set;
import javax.persistence.Embeddable;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Dramas {

    @ManyToMany
    private Set<Drama> dramas;
}
