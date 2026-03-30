package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

import javafx.scene.control.Label;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;
import sun.misc.Unsafe;

public class PersonCardTest {

    @Test
    public void getAddressValue_addressPresent_returnsAddress() {
        Person person = new PersonBuilder().withAddress("123 Clementi Road").build();

        assertEquals("123 Clementi Road", PersonCard.getAddressValue(person));
    }

    @Test
    public void getAddressValue_addressMissing_returnsEmptyString() {
        Person person = new PersonBuilder().withoutAddress().build();

        assertEquals("", PersonCard.getAddressValue(person));
    }

    @Test
    public void renderAddress_nullLabel_throwsNullPointerExceptionAfterAddressExtraction() throws Exception {
        PersonCard personCard = newUninitializedPersonCard();
        Person person = new PersonBuilder().withAddress("123 Clementi Road").build();
        Method renderAddressMethod = PersonCard.class.getDeclaredMethod("renderAddress", Person.class, Label.class);
        renderAddressMethod.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () ->
                renderAddressMethod.invoke(personCard, person, null));

        assertTrue(thrown.getCause() instanceof NullPointerException);
    }

    private PersonCard newUninitializedPersonCard() throws Exception {
        Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);
        return (PersonCard) unsafe.allocateInstance(PersonCard.class);
    }
}
