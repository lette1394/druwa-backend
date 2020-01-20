package me.druwa.be.domain.drama_tag;

interface DramaTagRepositoryExtended {
    DramaTags findAll(final DramaTags dramaTags);

    DramaTags findAll(DramaTagSearchStrings searchWord);
}
