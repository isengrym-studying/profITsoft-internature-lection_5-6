package testEntities;

import lombok.*;
import ua.klieshchunov.taskTwo.annotations.Property;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UnsupportedFieldTypeTestEntity {
    private String stringProperty;
    @Property("numberProperty")
    private Integer number;
    @Property(value = "timeProperty", format = "d-MMM-yyyy")
    private LocalDateTime time;
}
