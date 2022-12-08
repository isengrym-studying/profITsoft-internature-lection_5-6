package ua.klieshchunov.taskTwo.exceptions;

public class PropertiesLoadException extends RuntimeException {
    public PropertiesLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
