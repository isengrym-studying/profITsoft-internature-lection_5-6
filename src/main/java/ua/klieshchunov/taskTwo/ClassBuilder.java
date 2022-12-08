package ua.klieshchunov.taskTwo;

import ua.klieshchunov.taskTwo.annotations.Property;
import ua.klieshchunov.taskTwo.casters.ClassCaster;
import ua.klieshchunov.taskTwo.casters.ClassCasterFactory;
import ua.klieshchunov.taskTwo.entity.PropertyObject;
import ua.klieshchunov.taskTwo.exceptions.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Properties;

public class ClassBuilder {
    public static <T> T loadFromProperties(Class<T> cls, Path propertiesPath) {
        Properties properties = loadProperties(propertiesPath.toString());
        Field[] declaredFields = cls.getDeclaredFields();
        T instance = createInstance(cls);

        for (Field field : declaredFields) {
            Property fieldPropertyAnnotation = field.getAnnotation(Property.class);
            PropertyObject propertyObj = formPropertyObject(field, fieldPropertyAnnotation);
            propertyObj.value = (String)getPropertyValue(propertiesPath, properties, propertyObj.value);

            ClassCaster<?> classCaster = ClassCasterFactory.getClassCaster(field.getType());
            Method setter = getSetter(field);

            try {
                setter.invoke(instance, classCaster.cast(propertyObj));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new MethodInvocationException(String.format("Couldn't invoke method='%s' on class='%s'.", setter.getName(), cls.getName()),e);
            }
        }

        return instance;
    }

    private static PropertyObject formPropertyObject(Field field, Property annotation) {
        if (annotation == null)
            return new PropertyObject(field.getName(), "");
        else
            return new PropertyObject(annotation.value(), annotation.format());
    }


    private static Object getPropertyValue(Path propertiesPath, Properties properties, String propertyKey) {
        Object propertyValue = properties.get(propertyKey);

        if (propertyValue == null) {
            throw new PropertyNotFoundException(String.format(
                    "Couldn't find property='%s' in property file. Path='%s'", propertyKey, propertiesPath));
        }

        return propertyValue;
    }

    private static Properties loadProperties(String path) {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(path));
        } catch (IOException cause) {
            throw new PropertiesLoadException(String.format("Couldn't load properties from path='%s'", path), cause);
        }

        return prop;
    }

    private static <T> T createInstance(Class<T> cls) throws InstanceCreationException {
        try {
            return cls.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException |InvocationTargetException | NoSuchMethodException e) {
            throw new InstanceCreationException(String.format("Couldn't create instance of a class='%s'", cls.getName()), e);
        }
    }

    private static Method getSetter(Field field) {
        Class<?> cls = field.getDeclaringClass();
        Method[] methods = cls.getMethods();
        String setterName = getSetterName(field);
        for (Method method : methods) {
            if (method.getName().equals(setterName))
                return method;
        }
        throw new MethodNotFoundException(String.format("Couldn't find method='%s' in class='%s'",setterName, field.getDeclaringClass().getName()));
    }

    private static String getSetterName(Field field) {
        String fieldName = field.getName();
        String fieldNameCapitalized = fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
        return "set" + fieldNameCapitalized;
    }
}
