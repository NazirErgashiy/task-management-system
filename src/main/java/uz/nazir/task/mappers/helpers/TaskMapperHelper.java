package uz.nazir.task.mappers.helpers;

import org.springframework.stereotype.Component;
import uz.nazir.task.entities.User;

@Component
public class TaskMapperHelper {

    public User userIdMapper(Long value) {
        User user = new User();
        user.setId(value);
        return user;
    }

    public Long userIdMapper(User value) {
        return value.getId();
    }
}
