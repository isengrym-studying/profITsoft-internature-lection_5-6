package ua.klieshchunov.taskTwo.casters;

import ua.klieshchunov.taskTwo.entity.PropertyObject;

public interface ClassCaster <T>{
    T cast(PropertyObject value);
}
