package me.druwa.be.domain.drama_tag;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DramaTagService {
    private final DramaTagRepository dramaTagRepository;

    @Transactional
    public DramaTags createIfNotExists(final DramaTags newDramaTags) {
        final DramaTags existed = dramaTagRepository.tryFindAll(newDramaTags);
        final DramaTags needToSave = existed.filter(newDramaTags);

        return dramaTagRepository.saveAll(needToSave);
    }

    @Transactional
    public DramaTags findAll() {
        return DramaTags.dramaTags(dramaTagRepository.findAll());
    }
}