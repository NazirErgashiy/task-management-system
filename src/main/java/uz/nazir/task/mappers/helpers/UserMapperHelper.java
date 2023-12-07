package uz.nazir.task.mappers.helpers;

import org.springframework.stereotype.Component;
import uz.nazir.task.entities.Task;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapperHelper {

    public List<Long> taskIdMapping(List<Task> value) {
        if (value == null) return new ArrayList<>();

        List<Long> result = new ArrayList<>();
        for (Task task : value) {
            result.add(task.getId());
        }
        return result;
    }

    public List<Task> tasksIdMapping(List<Long> value) {
        if (value == null) return new ArrayList<>();

        List<Task> result = new ArrayList<>();
        for (Long id : value) {
            Task task = new Task();
            task.setId(id);
            result.add(task);
        }
        return result;
    }
}
