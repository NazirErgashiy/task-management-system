package uz.nazir.task.mappers.helpers;

import org.springframework.stereotype.Component;
import uz.nazir.task.entities.Comment;
import uz.nazir.task.entities.User;

import java.util.ArrayList;
import java.util.List;

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

    public List<Long> map(List<Comment> value) {
        if (value == null) return new ArrayList<>();

        List<Long> result = new ArrayList<>();
        for (Comment comment : value) {
            result.add(comment.getId());
        }
        return result;
    }
}
