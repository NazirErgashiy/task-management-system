package uz.nazir.task.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uz.nazir.task.dto.request.UserDtoRequest;
import uz.nazir.task.dto.response.UserDtoResponse;
import uz.nazir.task.entities.User;
import uz.nazir.task.mappers.helpers.UserMapperHelper;

@Mapper(componentModel = "spring", uses = {UserMapperHelper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    @Mapping(target = "tasksPerformer", ignore = true)
    @Mapping(target = "tasksAuthor", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    User dtoToEntity(UserDtoRequest request);

    @Mapping(source = "tasksPerformer", target = "tasksPerformerId")
    @Mapping(source = "tasksAuthor", target = "tasksAuthorId")
    UserDtoResponse entityToDto(User entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tasksPerformer", ignore = true)
    @Mapping(target = "tasksAuthor", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "password", ignore = true)
    void toEntity(UserDtoRequest request, @MappingTarget User user);
}
