package com.niz.d.playerplaylist.util;

/**
 * Factory for get a song.
 */
public class SongUtil {

    /**
     * Base url to mp3 song.
     */
    private static final String BASE_URL = "http://s.voicecards.ru/c/";

    /**
     * Extension for song.
     */
    private static final String SONG_EXTENSION = ".mp3";

    private SongUtil() {
    }

    /**
     * Gets url for a song. Urls must be a valid.
     *
     * @return Url for a song.
     */
    public static String getUrl(int id) {
        StringBuffer buffer = new StringBuffer();
        buffer
                .append(BASE_URL)
                .append(String.valueOf(id))
                .append(SONG_EXTENSION);

        return buffer.toString();
    }
}
