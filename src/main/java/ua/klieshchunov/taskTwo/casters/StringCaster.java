package ua.klieshchunov.taskTwo.casters;

import ua.klieshchunov.taskTwo.entity.PropertyObject;

public class StringCaster implements ClassCaster<String> {
    @Override
    public String cast(PropertyObject pObj) {
        return pObj.value;
    }
}
