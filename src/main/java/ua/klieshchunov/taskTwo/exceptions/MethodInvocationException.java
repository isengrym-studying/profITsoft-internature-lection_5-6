package ua.klieshchunov.taskTwo.exceptions;

public class MethodInvocationException extends RuntimeException {
    public MethodInvocationException(String message, Throwable cause) {
        super(message, cause);
    }
}
