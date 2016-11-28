package com.depop.countrypicker;

public class EmojiUtils {

    public static String getRegionalIndicatorSymbol(final char character) {
        if (character < 'A' || character > 'Z') {
            throw new IllegalArgumentException("Invalid character: you must use A-Z");
        }
        return String.valueOf(Character.toChars(0x1F1A5 + character));
    }
}
