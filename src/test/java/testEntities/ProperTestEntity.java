package testEntities;

import lombok.*;
import ua.klieshchunov.taskTwo.annotations.Property;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProperTestEntity {
    private String stringProperty;
    @Property("numberProperty")
    private Integer number;
    @Property(value = "timeProperty", format = "dd-MM-yyyy")
    private Instant time;
}
