package com.mamoori.mamooriback.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StringUtil {
    public static LocalDateTime strToDate(String str, String format) {
        return LocalDateTime.parse((CharSequence) str, DateTimeFormatter.ofPattern(format));
    }
}
