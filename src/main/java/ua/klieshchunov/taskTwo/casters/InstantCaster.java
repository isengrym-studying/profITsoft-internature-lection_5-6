package ua.klieshchunov.taskTwo.casters;

import ua.klieshchunov.taskTwo.entity.PropertyObject;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class InstantCaster implements ClassCaster<Instant> {
    @Override
    public Instant cast(PropertyObject pObj) {
        final DateTimeFormatter FMT = new DateTimeFormatterBuilder()
                .appendPattern(pObj.format)
                .parseDefaulting(ChronoField.NANO_OF_DAY, 0)
                .toFormatter()
                .withZone(ZoneId.of("Europe/Kiev"));

        return FMT.parse(pObj.value, Instant::from);
    }
}
