import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import testEntities.*;
import ua.klieshchunov.taskTwo.ClassBuilder;
import ua.klieshchunov.taskTwo.exceptions.*;

import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ClassBuilderTest {
    public static final String propertiesPath = "src/main/resources/class.properties";

    @Test
    public void loadFromProperties_shouldReturnProperInstance(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        Instant timeFormatted = LocalDate.parse("12-05-2017", formatter).atStartOfDay(ZoneOffset.UTC).toInstant();

        ProperTestEntity actual = ClassBuilder.loadFromProperties(ProperTestEntity.class, Path.of(propertiesPath));
        ProperTestEntity expected = new ProperTestEntity("someString", 7, timeFormatted);
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

