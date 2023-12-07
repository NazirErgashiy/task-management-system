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
import uz.nazir.task.dto.request.UserDtoRequest;
import uz.nazir.task.dto.response.UserDtoResponse;
import uz.nazir.task.entities.enums.Role;
import uz.nazir.task.services.impl.UserService;
import uz.nazir.task.validators.RoleValidator;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users", produces = "application/json")
public class UserController {

    private final UserService userService;
    private final RoleValidator roleValidator;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<UserDtoResponse> readPaged(
            @PageableDefault(page = 0, size = 50)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "name", direction = Sort.Direction.ASC)
            }) Pageable pageable
    ) {
        return userService.readAll(pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDtoResponse readById(@PathVariable Long id) {
        return userService.readById(id);
    }

    //USERS CREATED IN AUTHENTICATION_CONTROLLER
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDtoResponse create(@RequestBody
                                  @Validated
                                  UserDtoRequest request) {
        roleValidator.availableRoles(new Role[]{Role.ADMIN});
        return userService.create(request);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable Long id,
                       @RequestBody
                       @Validated
                       UserDtoRequest request) {
        roleValidator.canEditOnlySelfElements(request.getEmail(), new Role[]{Role.USER});
        userService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        roleValidator.availableRoles(new Role[]{Role.ADMIN});
        userService.deleteById(id);
    }
}
