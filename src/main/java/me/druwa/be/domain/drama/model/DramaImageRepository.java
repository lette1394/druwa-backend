package me.druwa.be.domain.drama.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import static me.druwa.be.domain.drama.model.DramaImages.dramaImages;

@Repository
public interface DramaImageRepository extends JpaRepository<DramaImage, Long> {

    default DramaImages saveAll(DramaImages dramaImages) {
        return dramaImages(saveAll(dramaImages.toRaw()));
    }
}
