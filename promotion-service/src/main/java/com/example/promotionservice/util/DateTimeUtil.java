package com.example.promotionservice.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Slf4j
public class DateTimeUtil {

    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN);

    /**
     * Parse date string to LocalDateTime with start of day (00:00:00)
     */
    public static LocalDateTime parseToStartOfDay(String dateStr) {
        return parseToStartOfDay(dateStr, DEFAULT_DATE_PATTERN);
    }

    /**
     * Parse date string with custom pattern to LocalDateTime with start of day
     */
    public static LocalDateTime parseToStartOfDay(String dateStr, String pattern) {
        return Optional.ofNullable(dateStr)
                .filter(StringUtils::hasText)
                .map(str -> {
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                        LocalDate date = LocalDate.parse(str, formatter);
                        return date.atStartOfDay();
                    } catch (DateTimeParseException e) {
                        log.error("Failed to parse date: {}, pattern: {}", dateStr, pattern, e);
                        throw new IllegalArgumentException("Invalid date format: " + dateStr, e);
                    }
                })
                .orElse(null);
    }

    /**
     * Parse date string to LocalDateTime with end of day (23:59:59)
     */
    public static LocalDateTime parseToEndOfDay(String dateStr) {
        return parseToEndOfDay(dateStr, DEFAULT_DATE_PATTERN);
    }

    /**
     * Parse date string with custom pattern to LocalDateTime with end of day
     */
    public static LocalDateTime parseToEndOfDay(String dateStr, String pattern) {
        return Optional.ofNullable(dateStr)
                .filter(StringUtils::hasText)
                .map(str -> {
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                        LocalDate date = LocalDate.parse(str, formatter);
                        return date.atTime(23, 59, 59);
                    } catch (DateTimeParseException e) {
                        log.error("Failed to parse date: {}, pattern: {}", dateStr, pattern, e);
                        throw new IllegalArgumentException("Invalid date format: " + dateStr, e);
                    }
                })
                .orElse(null);
    }

    /**
     * Parse date string to LocalDateTime with custom time
     */
    public static LocalDateTime parseToDateTime(String dateStr, int hour, int minute, int second) {
        return parseToDateTime(dateStr, DEFAULT_DATE_PATTERN, hour, minute, second);
    }

    public static LocalDateTime parseToDateTime(String dateStr, String pattern, int hour, int minute, int second) {
        return Optional.ofNullable(dateStr)
                .filter(StringUtils::hasText)
                .map(str -> {
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                        LocalDate date = LocalDate.parse(str, formatter);
                        return date.atTime(hour, minute, second);
                    } catch (DateTimeParseException e) {
                        log.error("Failed to parse date: {}, pattern: {}", dateStr, pattern, e);
                        throw new IllegalArgumentException("Invalid date format: " + dateStr, e);
                    }
                })
                .orElse(null);
    }

    /**
     * Format LocalDateTime to String with default pattern
     */
    public static String format(LocalDateTime dateTime) {
        return format(dateTime, DEFAULT_DATETIME_PATTERN);
    }

    public static String format(LocalDateTime dateTime, String pattern) {
        return Optional.ofNullable(dateTime)
                .map(dt -> dt.format(DateTimeFormatter.ofPattern(pattern)))
                .orElse(null);
    }

    /**
     * Format LocalDateTime to date string (yyyy-MM-dd)
     */
    public static String formatToDate(LocalDateTime dateTime) {
        return Optional.ofNullable(dateTime)
                .map(dt -> dt.format(DATE_FORMATTER))
                .orElse(null);
    }

    /**
     * Check if a date is between start and end (inclusive)
     */
    public static boolean isBetween(LocalDateTime checkDate, LocalDateTime startDate, LocalDateTime endDate) {
        if (checkDate == null || startDate == null || endDate == null) {
            return false;
        }
        return !checkDate.isBefore(startDate) && !checkDate.isAfter(endDate);
    }

    /**
     * Validate date range (startDate <= endDate)
     */
    public static boolean isValidRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            return false;
        }
        return !startDate.isAfter(endDate);
    }

    /**
     * Get current date time
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * Get today at start of day (00:00:00)
     */
    public static LocalDateTime todayStart() {
        return LocalDate.now().atStartOfDay();
    }

    /**
     * Get today at end of day (23:59:59)
     */
    public static LocalDateTime todayEnd() {
        return LocalDate.now().atTime(23, 59, 59);
    }
}