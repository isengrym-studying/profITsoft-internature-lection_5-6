package ua.klieshchunov.taskTwo.casters;

import ua.klieshchunov.taskTwo.entity.PropertyObject;

public class IntegerCaster implements ClassCaster<Integer> {
    @Override
    public Integer cast(PropertyObject pObj) {
        return Integer.valueOf(pObj.value);
    }
}
