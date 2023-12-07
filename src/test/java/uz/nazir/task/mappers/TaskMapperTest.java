package uz.nazir.task.mappers;

import org.junit.jupiter.api.Test;
import uz.nazir.task.dto.request.TaskDtoRequest;
import uz.nazir.task.entities.Task;
import uz.nazir.task.entities.User;
import uz.nazir.task.entities.enums.Priority;
import uz.nazir.task.entities.enums.Status;
import uz.nazir.task.mappers.helpers.TaskMapperHelper;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskMapperTest {

    private TaskMapper taskMapper = new TaskMapperImpl(new TaskMapperHelper());

    @Test
    void dtoToEntity() {
        var expected = TaskDtoRequest.builder()
                .header("header")
                .description("description")
                .priority(Priority.HIGH.name())
                .status(Status.EXPECTATION.name())
                .taskAuthorId(1L)
                .taskPerformerId(2L)
                .build();

        var result = taskMapper.dtoToEntity(expected);

        assertEquals(expected.getHeader(), result.getHeader());
        assertEquals(expected.getDescription(), result.getDescription());
        assertEquals(expected.getPriority(), result.getPriority().name());
        assertEquals(expected.getStatus(), result.getStatus().name());
        assertEquals(expected.getTaskAuthorId(), result.getTaskAuthor().getId());
        assertEquals(expected.getTaskPerformerId(), result.getTaskPerformer().getId());

        assertNull(result.getId());
        assertNull(result.getCreateDate());
        assertNull(result.getUpdateDate());
    }

    @Test
    void entityToDto() {
        LocalDateTime now = LocalDateTime.now();

        var expected = Task.builder()
                .id(1L)
                .header("header")
                .description("description")
                .status(Status.EXPECTATION)
                .priority(Priority.HIGH)
                .taskAuthor(User.builder().id(1L).build())
                .taskPerformer(User.builder().id(2L).build())
                .createDate(now)
                .updateDate(now)
                .build();

        var result = taskMapper.entityToDto(expected);

        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getHeader(), result.getHeader());
        assertEquals(expected.getDescription(), result.getDescription());
        assertEquals(expected.getPriority().name(), result.getPriority().name());
        assertEquals(expected.getStatus().name(), result.getStatus().name());
        assertEquals(expected.getTaskAuthor().getId(), result.getTaskAuthorId());
        assertEquals(expected.getTaskPerformer().getId(), result.getTaskPerformerId());
        assertEquals(expected.getCreateDate(), result.getCreateDate());
        assertEquals(expected.getUpdateDate(), result.getUpdateDate());
    }

    @Test
    void toEntity() {

        var expected = TaskDtoRequest.builder()
                .header("header")
                .description("description")
                .priority(Priority.HIGH.name())
                .status(Status.EXPECTATION.name())
                .taskAuthorId(1L)
                .taskPerformerId(2L)
                .build();

        LocalDateTime now = LocalDateTime.now();

        var result = Task.builder()
                .id(1L)
                .header("header")
                .description("description")
                .status(Status.EXPECTATION)
                .priority(Priority.HIGH)
                .taskAuthor(User.builder().id(1L).build())
                .taskPerformer(User.builder().id(2L).build())
                .createDate(now)
                .updateDate(now)
                .build();

        taskMapper.toEntity(expected, result);

        assertEquals(expected.getHeader(), result.getHeader());
        assertEquals(expected.getDescription(), result.getDescription());
        assertEquals(expected.getPriority(), result.getPriority().name());
        assertEquals(expected.getStatus(), result.getStatus().name());
        assertEquals(expected.getTaskAuthorId(), result.getTaskAuthor().getId());
        assertEquals(expected.getTaskPerformerId(), result.getTaskPerformer().getId());
        assertNotNull(result.getId());
        assertNotNull(result.getCreateDate());
        assertNotNull(result.getUpdateDate());
    }
}