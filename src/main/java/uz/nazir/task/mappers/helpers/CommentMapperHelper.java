package uz.nazir.task.mappers.helpers;

import org.springframework.stereotype.Component;
import uz.nazir.task.entities.Task;
import uz.nazir.task.entities.User;

@Component
public class CommentMapperHelper {

    public Task taskIdMapper(Long value) {
        return Task.builder()
                .id(value)
                .build();
    }

    public User userIdMapper(Long value) {
        return User.builder()
                .id(value)
                .build();
    }

    public Long map(Task value) {
        return value.getId();
    }

    public Long map(User value) {
        return value.getId();
    }
}
