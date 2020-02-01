package me.druwa.be.domain.drama_tag;

interface DramaTagRepositoryExtended {
    DramaTags search(final DramaTags dramaTags);

    DramaTags search(DramaTagSearchStrings searchWord);
}
