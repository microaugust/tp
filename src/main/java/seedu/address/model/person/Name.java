package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String MESSAGE_CONSTRAINTS =
            "Names should not be blank, and should only contain alphabets, \"-\", \",\", \".\", \"'\", and \" \".";

    /*
     * Requires the name to start with an alphabetic character.
     * This rejects blank inputs and names that begin with whitespace or punctuation.
     */
    public static final String VALIDATION_REGEX = "[A-Za-z][A-Za-z ,.'\\-]*";

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
        fullName = name;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Name)) {
            return false;
        }

        Name otherName = (Name) other;

        // equality check is case-insensitive
        String fullNameLower = fullName.toLowerCase();
        String otherFullNameLower = otherName.fullName.toLowerCase();
        return fullNameLower.equals(otherFullNameLower);
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}
