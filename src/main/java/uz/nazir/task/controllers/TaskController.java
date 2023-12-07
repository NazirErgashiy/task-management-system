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
import uz.nazir.task.dto.request.TaskDtoRequest;
import uz.nazir.task.dto.response.TaskDtoResponse;
import uz.nazir.task.entities.enums.Role;
import uz.nazir.task.services.impl.TaskService;
import uz.nazir.task.validators.RoleValidator;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/tasks", produces = "application/json")
public class TaskController {

    private final TaskService taskService;
    private final RoleValidator roleValidator;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<TaskDtoResponse> readPaged(
            @PageableDefault(page = 0, size = 50)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "status", direction = Sort.Direction.ASC)
            }) Pageable pageable
    ) {
        return taskService.readAll(pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDtoResponse readById(@PathVariable Long id) {
        return taskService.readById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDtoResponse create(@RequestBody
                                  @Validated
                                  TaskDtoRequest request) {
        return taskService.create(request);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable Long id,
                       @RequestBody
                       @Validated
                       TaskDtoRequest request) {
        roleValidator.canEditOnlySelfElements(id, new Role[]{Role.USER});
        taskService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        roleValidator.canEditOnlySelfElements(id, new Role[]{Role.USER});
        taskService.deleteById(id);
    }
}
