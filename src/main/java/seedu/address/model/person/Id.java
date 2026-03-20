package seedu.address.model.person;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a Person's id in the address book.
 * Guarantees: immutable.
 */
public class Id implements Comparable<Id> {
    public static final String MESSAGE_CONSTRAINTS =
            "Ids should not be blank, and must be a positive integer.";
    private static final int SMALLEST_VALUE = 1;
    public final int value;

    /**
     * Constructs the smallest possible {@code Id}.
     */
    private Id() {
        this.value = SMALLEST_VALUE;
    }

    private Id(int value) {
        this.value = value;
    }

    private Id(Id currentMaxId) {
        // increment by 1 to avoid duplicated ids
        this.value = currentMaxId.value + 1;
    }

    /**
     * Creates a new {@code Id} with the smallest possible value.
     */
    public static Id minimumPossible() {
        return new Id();
    }

    /**
     * Creates a new {@code Id} with the specified value.
     */
    public static Id of(int value) {
        return new Id(value);
    }

    /**
     * Creates a new {@code Id} using the maximum id that is saved in the address book currently.
     */
    public static Id fromCurrentMaxId(Id currentMaxId) {
        return new Id(currentMaxId);
    }

    /**
     * Returns true if a given int is a valid id.
     */
    public static boolean isValidId(int test) {
        return test >= SMALLEST_VALUE;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("id", this.value).toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Id)) {
            return false;
        }

        Id otherId = (Id) other;
        return this.value == otherId.value;
    }

    @Override
    public int compareTo(Id other) {
        return this.value - other.value;
    }

    @Override
    public int hashCode() {
        return this.value;
    }

}
