package testEntities;

import lombok.*;
import ua.klieshchunov.taskTwo.annotations.Property;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class WrongDateFormatTestEntity {
    private String stringProperty;
    @Property("numberProperty")
    private Integer number;
    @Property(value = "timeProperty", format = "d-MMM-yyyy")
    private Instant time;
}
