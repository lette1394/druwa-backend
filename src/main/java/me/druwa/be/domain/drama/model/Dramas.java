package me.druwa.be.domain.drama.model;

import java.util.Set;
import javax.persistence.Embeddable;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
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


}
