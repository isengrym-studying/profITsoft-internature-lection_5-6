package ua.klieshchunov.taskTwo.exceptions;

public class MethodNotFoundException extends RuntimeException {
    public MethodNotFoundException(String message) {
        super(message);
    }
}
