package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains unit tests for {@code TagCommand}.
 */
public class TagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToTag = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, new Tag("Student"));
        Person taggedPerson = new PersonBuilder(personToTag).withTags("Student").build();

        String expectedMessage = String.format(TagCommand.MESSAGE_TAG_PERSON_SUCCESS, "Student",
                INDEX_SECOND_PERSON.getOneBased());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToTag, taggedPerson);

        TagCommand command = new TagCommand(INDEX_SECOND_PERSON, new Tag("Student"));
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToTag = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person taggedPerson = new PersonBuilder(personToTag).withTags("Tutor").build();
        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, new Tag("Tutor"));

        String expectedMessage = String.format(TagCommand.MESSAGE_TAG_PERSON_SUCCESS, "Tutor",
                INDEX_FIRST_PERSON.getOneBased());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updateFilteredPersonList(matchPersonName(personToTag));
        expectedModel.setPerson(personToTag, taggedPerson);

        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        TagCommand tagCommand = new TagCommand(outOfBoundIndex, new Tag("Parent"));

        assertCommandFailure(tagCommand, model,
                String.format(TagCommand.MESSAGE_INVALID_PERSON_INDEX, outOfBoundIndex.getOneBased()));
    }

    @Test
    public void execute_invalidIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;

        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        TagCommand tagCommand = new TagCommand(outOfBoundIndex, new Tag("Parent"));

        assertCommandFailure(tagCommand, model,
                String.format(TagCommand.MESSAGE_INVALID_PERSON_INDEX, outOfBoundIndex.getOneBased()));
    }

    @Test
    public void equals() {
        TagCommand firstCommand = new TagCommand(INDEX_FIRST_PERSON, new Tag("Student"));
        TagCommand firstCommandCopy = new TagCommand(INDEX_FIRST_PERSON, new Tag("Student"));
        TagCommand secondCommand = new TagCommand(INDEX_SECOND_PERSON, new Tag("Tutor"));

        assertTrue(firstCommand.equals(firstCommand));
        assertTrue(firstCommand.equals(firstCommandCopy));
        assertFalse(firstCommand.equals(1));
        assertFalse(firstCommand.equals(null));
        assertFalse(firstCommand.equals(secondCommand));
    }

    @Test
    public void toStringMethod() {
        Tag categoryTag = new Tag("Student");
        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, categoryTag);
        String expected = TagCommand.class.getCanonicalName()
                + "{targetIndex=" + INDEX_FIRST_PERSON + ", categoryTag=" + categoryTag + "}";
        assertEquals(expected, tagCommand.toString());
    }

    private static Predicate<Person> matchPersonName(Person person) {
        return candidate -> candidate.getName().equals(person.getName());
    }
}
