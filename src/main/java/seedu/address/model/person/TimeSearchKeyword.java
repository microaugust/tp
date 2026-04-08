package seedu.address.model.person;

/**
 * Represents a parsed time-search keyword for the find command.
 *
 * @param day canonical weekday name, or empty string if the query omitted day
 * @param time canonical time or duration, or empty string if the query omitted time
 */
public record TimeSearchKeyword(String day, String time) { }
