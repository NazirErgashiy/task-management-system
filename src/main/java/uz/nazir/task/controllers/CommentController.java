package uz.nazir.task.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uz.nazir.task.dto.request.CommentDtoRequest;
import uz.nazir.task.dto.response.CommentDtoResponse;
import uz.nazir.task.entities.enums.Role;
import uz.nazir.task.services.impl.CommentService;
import uz.nazir.task.validators.RoleValidator;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/comments", produces = "application/json")
public class CommentController {

    private final CommentService commentService;
    private final RoleValidator roleValidator;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<CommentDtoResponse> readPaged(
            @PageableDefault(page = 0, size = 50)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            }) Pageable pageable
    ) {
        return commentService.readAll(pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDtoResponse readById(@PathVariable Long id) {
        return commentService.readById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDtoResponse create(@RequestBody
                                     @Validated
                                     CommentDtoRequest request) {
        return commentService.create(request);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable Long id,
                       @RequestBody
                       @Validated
                       CommentDtoRequest request) {
        roleValidator.canEditOnlySelfElementsComment(id, new Role[]{Role.USER});
        commentService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        roleValidator.canEditOnlySelfElementsComment(id, new Role[]{Role.USER});
        commentService.deleteById(id);
    }
}
