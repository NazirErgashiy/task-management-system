package uz.nazir.task.mappers;

import org.junit.jupiter.api.Test;
import uz.nazir.task.dto.request.UserDtoRequest;
import uz.nazir.task.entities.Task;
import uz.nazir.task.entities.User;
import uz.nazir.task.entities.enums.Role;
import uz.nazir.task.mappers.helpers.UserMapperHelper;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper mapper = new UserMapperImpl(new UserMapperHelper());

    @Test
    void dtoToEntity() {
        var expected = UserDtoRequest.builder()
                .name("TestName")
                .email("test@mail.com")
                .role(Role.USER.name())
                .build();

        var result = mapper.dtoToEntity(expected);

        assertEquals(expected.getName(), result.getName());
        assertEquals(expected.getEmail(), result.getEmail());
        assertEquals(expected.getRole(), result.getRole().name());

        assertNull(result.getId());
        assertNull(result.getPassword());
        assertNull(result.getCreateDate());
        assertNull(result.getUpdateDate());
    }

    @Test
    void entityToDto() {
        LocalDateTime now = LocalDateTime.now();

        var expected = User
                .builder()
                .id(1L)
                .name("TestName")
                .email("test@mail.com")
                .role(Role.USER)
                .password("TestPassword")
                .tasksAuthor(List.of(Task.builder().id(1L).build()))
                .tasksPerformer(List.of(Task.builder().id(2L).build()))
                .createDate(now)
                .updateDate(now)
                .build();

        var result = mapper.entityToDto(expected);

        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getName(), result.getName());
        assertEquals(expected.getEmail(), result.getEmail());
        assertEquals(expected.getRole().name(), result.getRole().name());
        assertEquals(expected.getCreateDate(), result.getCreateDate());
        assertEquals(expected.getUpdateDate(), result.getUpdateDate());
        assertEquals(expected.getTasksAuthor().get(0).getId(), result.getTasksAuthorId().get(0));
        assertEquals(expected.getTasksPerformer().get(0).getId(), result.getTasksPerformerId().get(0));
    }

    @Test
    void toEntity() {

        var expected = UserDtoRequest.builder()
                .name("TestName")
                .email("test@mail.com")
                .role(Role.USER.name())
                .build();

        LocalDateTime now = LocalDateTime.now();
        var result = User
                .builder()
                .id(1L)
                .name("TestName")
                .email("test@mail.com")
                .role(Role.USER)
                .password("TestPassword")
                .tasksAuthor(List.of(Task.builder().id(1L).build()))
                .tasksPerformer(List.of(Task.builder().id(2L).build()))
                .createDate(now)
                .updateDate(now)
                .build();

        mapper.toEntity(expected,result);

        assertEquals(expected.getName(), result.getName());
        assertEquals(expected.getEmail(), result.getEmail());
        assertEquals(expected.getRole(), result.getRole().name());

        assertNotNull(result.getId());
        assertNotNull(result.getPassword());
        assertNotNull(result.getCreateDate());
        assertNotNull(result.getUpdateDate());
    }
}