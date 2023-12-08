package uz.nazir.task.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uz.nazir.task.dto.request.CommentDtoRequest;
import uz.nazir.task.dto.response.CommentDtoResponse;
import uz.nazir.task.entities.Comment;
import uz.nazir.task.mappers.helpers.CommentMapperHelper;

@Mapper(componentModel = "spring", uses = {CommentMapperHelper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CommentMapper {

    @Mapping(source = "taskId", target = "task")
    @Mapping(source = "authorId", target = "author")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    Comment dtoToEntity(CommentDtoRequest request);

    @Mapping(source = "task", target = "taskId")
    @Mapping(source = "author", target = "authorId")
    CommentDtoResponse entityToDto(Comment entity);

    @Mapping(source = "taskId", target = "task")
    @Mapping(source = "authorId", target = "author")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    void toEntity(CommentDtoRequest request, @MappingTarget Comment comment);
}
