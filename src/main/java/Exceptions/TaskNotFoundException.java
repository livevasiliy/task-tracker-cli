package Exceptions;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException() {
        super("Task not found");
    }
}
