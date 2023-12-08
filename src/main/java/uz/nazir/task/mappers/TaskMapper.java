package uz.nazir.task.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uz.nazir.task.dto.request.TaskDtoRequest;
import uz.nazir.task.dto.response.TaskDtoResponse;
import uz.nazir.task.entities.Task;
import uz.nazir.task.mappers.helpers.TaskMapperHelper;

@Mapper(componentModel = "spring", uses = {TaskMapperHelper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TaskMapper {

    @Mapping(source = "taskAuthorId", target = "taskAuthor")
    @Mapping(source = "taskPerformerId", target = "taskPerformer")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "comments", ignore = true)
    Task dtoToEntity(TaskDtoRequest request);

    @Mapping(source = "taskAuthor", target = "taskAuthorId")
    @Mapping(source = "taskPerformer", target = "taskPerformerId")
    @Mapping(source = "comments", target = "commentsId")
    TaskDtoResponse entityToDto(Task entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(source = "taskAuthorId", target = "taskAuthor")
    @Mapping(source = "taskPerformerId", target = "taskPerformer")
    void toEntity(TaskDtoRequest request, @MappingTarget Task task);
}
