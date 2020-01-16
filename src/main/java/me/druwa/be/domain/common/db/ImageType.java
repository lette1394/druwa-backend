package me.druwa.be.domain.common.db;


import org.apache.commons.lang3.StringUtils;

public enum ImageType {
    JPG, JPEG, GIF, PNG, UNKNOWN;

    public static ImageType parse(final String str) {
        try {
            return ImageType.valueOf(StringUtils.upperCase(str));
        } catch (Exception e) {
            return UNKNOWN;
        }
    }
}
