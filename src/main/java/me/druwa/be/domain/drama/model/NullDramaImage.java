package me.druwa.be.domain.drama.model;


import javax.annotation.Nullable;

public class NullDramaImage extends DramaImage {

    public NullDramaImage() {
        super(null);
    }

    @Override
    @Nullable
    public String toS3Url() {
        return null;
    }
}
