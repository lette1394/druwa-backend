package me.druwa.be.domain.common.cache;

public class CacheKey {
    public static class User {
        public static final String ID = "USER_ID";
        public static final String EMAIL = "USER_EMAIL";
        public static final String NAME = "USER_NAME";
    }

    public static class Drama {
        public static final String ID = "DRAMA_ID";
        public static final String EXISTS = "DRAMA_EXISTS";
    }

    public static class DramaEpisode {
        public static final String EXISTS = "DRAMA_EXISTS";
    }

    public static class DramaEpisodeComment {
        public static final String EXISTS = "DRAMA_EXISTS";
    }
}
