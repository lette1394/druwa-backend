package me.druwa.be.domain.drama.model;


import javax.annotation.Nullable;

public class NullDramaImage extends DramaImage {

    public NullDramaImage() {
        super();
    }

    @Override
    @Nullable
    public String toS3Url() {
        return null;
    }
}
