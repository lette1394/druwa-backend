package me.druwa.be.domain.drama.service;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import me.druwa.be.domain.drama.model.Drama;
import me.druwa.be.domain.drama.repository.DramaRepository;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class DramaService {
    private final DramaRepository repository;

    public Drama findBy(final long id) {
        return repository.findById(id)
                         .orElseThrow(() -> new NoSuchElementException(format("no drama with id:[%s]", id)));
    }

    public void ensureExistsBy(final long id) {
        if (repository.existsById(id)) {
            return;
        }
        throw new NoSuchElementException(format("no drama with id: [%s]", id));
    }
}
