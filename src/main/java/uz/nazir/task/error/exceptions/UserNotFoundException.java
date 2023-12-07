package uz.nazir.task.error.exceptions;

public class UserNotFoundException extends NotFoundException{
    public UserNotFoundException(Long id) {
        super("user", id);
    }
}
