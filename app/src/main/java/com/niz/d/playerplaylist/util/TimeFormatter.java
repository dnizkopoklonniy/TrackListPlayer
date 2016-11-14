package com.niz.d.playerplaylist.util;

/**
 * Formats time to the desired format for viewing it in genre oldCards.
 */
public class TimeFormatter {

    /**
     * Format for period value.
     */
    private String periodValueFormat = "%02d";

    /**
     * Period separator between hours, minutes, seconds.
     */
    private final String separator = ":";

    /**
     * Formats time to the desire in string to hh:mm:ss format.
     *
     * @param millis Milliseconds.
     * @return Formatted time string.
     */
    public String formatTime(long millis) {
        StringBuffer buffer = new StringBuffer();

        int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

        buffer
                .append(String.format(periodValueFormat, minutes))
                .append(separator)
                .append(String.format(periodValueFormat, seconds));

        return buffer.toString();
    }
}
