package ua.klieshchunov.taskTwo.casters;

import ua.klieshchunov.taskTwo.exceptions.UnsupportedTypeException;

import java.time.Instant;

public class ClassCasterFactory {
    public static ClassCaster<?> getClassCaster(Class<?> type) {
        if (type.equals(String.class))
            return new StringCaster();
        else if (type.equals(Integer.class))
            return new IntegerCaster();
        else if (type.equals(Instant.class))
            return new InstantCaster();
        throw new UnsupportedTypeException(String.format("Type='%s' is not supported by ClassCaster", type.getName()));
    }
}
