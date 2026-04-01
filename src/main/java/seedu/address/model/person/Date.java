package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Locale;

/**
 * Represents a Person's date in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidDate(String)}
 */
public class Date {

    public static final String MESSAGE_CONSTRAINTS =
            "Dates should be in the format yyyy-MM-dd or d/M/yyyy, and be a valid calendar date";
    private static final String EMPTY_STRING = "";
    private static final DateTimeFormatter DISPLAY_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM d uuuu",
            Locale.ENGLISH);
    private static final DateTimeFormatter STORAGE_DATE_FORMATTER = formatter("uuuu-MM-dd");
    private static final List<DateTimeFormatter> USER_DATE_FORMATTERS = List.of(
            STORAGE_DATE_FORMATTER,
            formatter("d/M/uuuu"));

    public final String value;
    private final LocalDate localDate;

    /**
     * Constructs a {@code Date}.
     *
     * @param date A valid date.
     */
    public Date(String date) {
        requireNonNull(date);
        checkArgument(isValidDate(date), MESSAGE_CONSTRAINTS);
        localDate = parseDate(date);
        value = localDate.format(STORAGE_DATE_FORMATTER);
    }

    private static DateTimeFormatter formatter(String pattern) {
        return DateTimeFormatter.ofPattern(pattern).withResolverStyle(ResolverStyle.STRICT);
    }

    /**
     * Returns true if a given string is a valid date.
     */
    public static boolean isValidDate(String test) {
        requireNonNull(test);
        return parseDate(test) != null;
    }

    private static LocalDate parseDate(String date) {
        String trimmedDate = date.trim();

        for (DateTimeFormatter formatter : USER_DATE_FORMATTERS) {
            try {
                return LocalDate.parse(trimmedDate, formatter);
            } catch (DateTimeParseException ignored) {
                // Try next format.
            }
        }

        return null;
    }

    /**
     * Returns the display value of the date.
     */
    public String getDisplayValue() {
        return localDate.format(DISPLAY_DATE_FORMATTER);
    }

    /**
     * Returns true if given string is a valid date or is empty.
     * Used for loading from the JSON storage file.
     *
     * @param date date given to check for validity.
     * @return true if valid date or empty, else false.
     */
    public static boolean isValidDateOrEmptyString(String date) {
        requireNonNull(date);
        return date.equals(EMPTY_STRING) || isValidDate(date);
    }

    @Override
    public String toString() {
        return getDisplayValue();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Date)) {
            return false;
        }

        Date otherDate = (Date) other;
        return value.equals(otherDate.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
