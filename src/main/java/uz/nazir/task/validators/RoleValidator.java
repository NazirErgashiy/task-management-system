package uz.nazir.task.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import uz.nazir.task.entities.Task;
import uz.nazir.task.entities.enums.Role;
import uz.nazir.task.error.exceptions.ForbiddenException;
import uz.nazir.task.error.exceptions.TaskNotFoundException;
import uz.nazir.task.repositories.TaskRepository;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class RoleValidator {

    private final TaskRepository taskRepository;

    public void availableRoles(Role[] roles) {
        List<SimpleGrantedAuthority> role = (List<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (Role r : roles) {
            if (r.name().equals(role.get(0).toString())) {
                return;
            }
        }
        throw new ForbiddenException("Role availability exception");
    }

    public void canEditOnlySelfElements(String gotEmail, Role[] roles) {
        String currentAuthEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<SimpleGrantedAuthority> role = (List<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        for (Role r : roles) {
            if (r.name().equals(role.get(0).toString())) {
                if (!Objects.equals(currentAuthEmail, gotEmail)) {
                    throw new ForbiddenException("Role availability exception: can edit only self elements");
                }
            }
        }
    }

    public void canEditOnlySelfElements(Long taskId, Role[] roles) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        String taskAuthorEmail = task.getTaskAuthor().getEmail();
        String taskPerformerEmail = task.getTaskPerformer().getEmail();

        if (!isCurrentUserEmailModifyingCurrentElement(taskAuthorEmail, roles) && !isCurrentUserEmailModifyingCurrentElement(taskPerformerEmail, roles)) {
            throw new ForbiddenException("Role availability exception: can edit only self elements");
        }
    }

    private boolean isCurrentUserEmailModifyingCurrentElement(String gotEmail, Role[] roles) {
        String currentAuthEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<SimpleGrantedAuthority> role = (List<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        for (Role r : roles) {
            if (r.name().equals(role.get(0).toString())) {
                if (!Objects.equals(currentAuthEmail, gotEmail)) {
                    return false;
                }
            }
        }
        return true;
    }
}
