package me.druwa.be.domain.common.db;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JoinTableName {
    public static final String DRAMA__HAS__DRAMA_TAG = "drama__has__drama_tag_";
    public static final String DRAMA_EPISODE__HAS__DRAMA_EPISODE_COMMENT = "drama_episode__has__drama_episode_comment_";


    public static final String USER__LIKES__DRAMA = "user__likes__drama_";
    public static final String USER__LIKES__DRAMA_EPISODE_COMMENT = "user__likes__drama_episode_comment_";
}
