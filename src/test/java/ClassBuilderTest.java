import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import testEntities.*;
import ua.klieshchunov.taskTwo.ClassBuilder;
import ua.klieshchunov.taskTwo.exceptions.*;

import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

public class ClassBuilderTest {
    public static final String propertiesPath = "src/main/resources/class.properties";

    @Test
    public void loadFromProperties_shouldReturnProperInstance(){
        DateTimeFormatter time = new DateTimeFormatterBuilder()
                .appendPattern("dd-MM-yyyy")
                .parseDefaulting(ChronoField.NANO_OF_DAY, 0)
                .toFormatter()
                .withZone(ZoneId.of("Europe/Kiev"));

        ProperTestEntity actual = ClassBuilder.loadFromProperties(ProperTestEntity.class, Path.of(propertiesPath));
        ProperTestEntity expected = new ProperTestEntity("someString", 7, time.parse("12-05-2017", Instant::from));
        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void loadFromProperties_shouldThrowPropertiesLoadException() {
        Assertions.assertThrows(PropertiesLoadException.class, () -> {
            ClassBuilder.loadFromProperties(ProperTestEntity.class, Path.of(propertiesPath + "sdsdg23fgsd"));
        });
    }

    @Test
    public void loadFromProperties_shouldThrowPropertyNotFoundException() {
        Assertions.assertThrows(PropertyNotFoundException.class, () -> {
            ClassBuilder.loadFromProperties(NonexistentPropertyNameTestEntity.class, Path.of(propertiesPath));
        });
    }

    @Test
    public void loadFromProperties_shouldThrowInstanceCreationException() {
        Assertions.assertThrows(InstanceCreationException.class, () -> {
            ClassBuilder.loadFromProperties(AbsentEmptyConstructorTestEntity.class, Path.of(propertiesPath));
        });
    }

    @Test
    public void loadFromProperties_shouldThrowMethodNotFoundException() {
        Assertions.assertThrows(MethodNotFoundException.class, () -> {
            ClassBuilder.loadFromProperties(AbsentSetterTestEntity.class, Path.of(propertiesPath));
        });
    }

    @Test
    public void loadFromProperties_shouldThrowUnsupportedTypeException() {
        Assertions.assertThrows(UnsupportedTypeException.class, () -> {
            ClassBuilder.loadFromProperties(UnsupportedFieldTypeTestEntity.class, Path.of(propertiesPath));
        });
    }

    @Test
    public void loadFromProperties_shouldThrowDateTimeParseException() {
        Assertions.assertThrows(DateTimeParseException.class, () -> {
            ClassBuilder.loadFromProperties(WrongDateFormatTestEntity.class, Path.of(propertiesPath));
        });
    }
}

