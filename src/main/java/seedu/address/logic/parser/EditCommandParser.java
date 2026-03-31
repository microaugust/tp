package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG_DELETE;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Id;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {
    private static final String TAG_RESET_VALUE = "";

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_ADDRESS, PREFIX_TAG,
                        PREFIX_TAG_DELETE);

        Id id;

        try {
            id = ParserUtil.parseId(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_ADDRESS);

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editPersonDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            Optional<Phone> optionalPhone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE));
            editPersonDescriptor.setPhone(optionalPhone);
        }
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            editPersonDescriptor.setAddress(ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get()));
        }
        Optional<Set<Tag>> tagsToAdd = parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG));
        Optional<Set<Tag>> tagsToDelete = parseTagsToDeleteForEdit(argMultimap.getAllValues(PREFIX_TAG_DELETE));

        validateTagEdits(tagsToAdd, tagsToDelete);

        tagsToAdd.ifPresent(editPersonDescriptor::setTags);
        tagsToDelete.ifPresent(editPersonDescriptor::setTagsToDelete);

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(id, editPersonDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        requireNonNull(tags);

        if (tags.isEmpty()) {
            return Optional.empty();
        }

        // A bare t/ clears all tags, but it cannot be combined with category or tdel/ values.
        if (isTagReset(tags)) {
            return Optional.of(Collections.emptySet());
        }
        if (containsTagResetValue(tags)) {
            throw new ParseException(EditCommand.MESSAGE_INVALID_TAG_RESET);
        }

        return Optional.of(ParserUtil.parseTags(tags));
    }

    private Optional<Set<Tag>> parseTagsToDeleteForEdit(Collection<String> tagsToDelete) throws ParseException {
        requireNonNull(tagsToDelete);

        if (tagsToDelete.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(ParserUtil.parseTags(tagsToDelete));
    }

    private void validateTagEdits(Optional<Set<Tag>> tagsToAdd, Optional<Set<Tag>> tagsToDelete)
            throws ParseException {
        requireNonNull(tagsToAdd);
        requireNonNull(tagsToDelete);

        if (isTagReset(tagsToAdd) && tagsToDelete.isPresent()) {
            throw new ParseException(EditCommand.MESSAGE_INVALID_TAG_RESET);
        }

        if (hasOverlappingTags(tagsToAdd, tagsToDelete)) {
            throw new ParseException(EditCommand.MESSAGE_CONFLICTING_TAG_EDITS);
        }
    }

    private static boolean isTagReset(Optional<Set<Tag>> tagsToAdd) {
        return tagsToAdd.isPresent() && tagsToAdd.get().isEmpty();
    }

    private static boolean hasOverlappingTags(Optional<Set<Tag>> tagsToAdd, Optional<Set<Tag>> tagsToDelete) {
        if (tagsToAdd.isEmpty() || tagsToDelete.isEmpty()) {
            return false;
        }

        Set<Tag> overlappingTags = new HashSet<>(tagsToAdd.get());
        overlappingTags.retainAll(tagsToDelete.get());
        return !overlappingTags.isEmpty();
    }

    private static boolean isTagReset(Collection<String> tags) {
        return tags.size() == 1 && containsTagResetValue(tags);
    }

    private static boolean containsTagResetValue(Collection<String> tags) {
        return tags.contains(TAG_RESET_VALUE);
    }

}
