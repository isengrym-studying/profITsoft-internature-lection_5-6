package testEntities;

import lombok.*;
import ua.klieshchunov.taskTwo.annotations.Property;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class AbsentEmptyConstructorTestEntity {
    @Property(value = "stringProperty")
    private String stringProperty;
    @Property(value = "numberProperty")
    private Integer number;
    @Property(value = "timeProperty", format = "d-MMM-yyyy")
    private Instant time;
}