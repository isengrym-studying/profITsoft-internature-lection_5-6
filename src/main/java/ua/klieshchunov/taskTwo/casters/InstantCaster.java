package ua.klieshchunov.taskTwo.casters;

import ua.klieshchunov.taskTwo.entity.PropertyObject;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class InstantCaster implements ClassCaster<Instant> {
    @Override
    public Instant cast(PropertyObject pObj) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pObj.format);
        return LocalDate.parse(pObj.value, formatter).atStartOfDay(ZoneOffset.UTC).toInstant();
    }
}
