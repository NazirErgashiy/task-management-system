package uz.nazir.task.error.exceptions;

public class TaskNotFoundException extends NotFoundException{
    public TaskNotFoundException(Long id) {
        super("task", id);
    }
}
