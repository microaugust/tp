package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Tags a contact with a category.
 */
public class TagCommand extends Command {

    public static final String COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Tags the person identified by the index number "
            + "used in the displayed person list with a category.\n"
            + "Parameters: INDEX " + PREFIX_TAG + "CATEGORY\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_TAG + "Student";

    public static final String MESSAGE_TAG_PERSON_SUCCESS =
            "Alright, the tag %1$s has been added to contact number %2$d.";
    public static final String MESSAGE_INVALID_PERSON_INDEX = "Index %1$d is not found in contact list.";

    private final Index targetIndex;
    private final Tag categoryTag;

    /**
     * Creates a TagCommand to tag the specified {@code Person}.
     */
    public TagCommand(Index targetIndex, Tag categoryTag) {
        requireNonNull(targetIndex);
        requireNonNull(categoryTag);
        this.targetIndex = targetIndex;
        this.categoryTag = categoryTag;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(MESSAGE_INVALID_PERSON_INDEX, targetIndex.getOneBased()));
        }

        Person personToTag = lastShownList.get(targetIndex.getZeroBased());
        Person taggedPerson = createTaggedPerson(personToTag, categoryTag);
        model.setPerson(personToTag, taggedPerson);

        return new CommandResult(String.format(MESSAGE_TAG_PERSON_SUCCESS,
                categoryTag.tagName, targetIndex.getOneBased()));
    }

    /**
     * Returns a copy of {@code personToTag} that keeps the existing identity fields
     * and replaces all current tags with the selected category tag.
     */
    private static Person createTaggedPerson(Person personToTag, Tag categoryTag) {
        return new Person(personToTag.getName(), personToTag.getPhone(),
                personToTag.getAddress(), Set.of(categoryTag));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof TagCommand)) {
            return false;
        }

        TagCommand otherTagCommand = (TagCommand) other;
        return targetIndex.equals(otherTagCommand.targetIndex)
                && categoryTag.equals(otherTagCommand.categoryTag);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("categoryTag", categoryTag)
                .toString();
    }
}
