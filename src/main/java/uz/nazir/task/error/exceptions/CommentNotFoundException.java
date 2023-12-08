package uz.nazir.task.error.exceptions;

public class CommentNotFoundException extends NotFoundException{
    public CommentNotFoundException(Long id) {
        super("comment", id);
    }
}
