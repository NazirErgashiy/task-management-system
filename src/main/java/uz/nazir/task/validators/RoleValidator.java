package uz.nazir.task.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import uz.nazir.task.dto.request.UserDtoRequest;
import uz.nazir.task.entities.Comment;
import uz.nazir.task.entities.Task;
import uz.nazir.task.entities.User;
import uz.nazir.task.entities.enums.Role;
import uz.nazir.task.error.exceptions.CommentNotFoundException;
import uz.nazir.task.error.exceptions.ForbiddenException;
import uz.nazir.task.error.exceptions.TaskNotFoundException;
import uz.nazir.task.repositories.CommentRepository;
import uz.nazir.task.repositories.TaskRepository;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class RoleValidator {

    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;

    public void availableRoles(Role[] roles) {
        List<SimpleGrantedAuthority> role = (List<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (Role r : roles) {
            if (r.name().equals(role.get(0).toString())) {
                return;
            }
        }
        throw new ForbiddenException("Role availability exception");
    }

    public void checkSelfEditing(Object checkObj, Role[] checkRoles) {
        Map<String, Object> credentials = (Map<String, Object>) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        User currentUser = (User) credentials.get("user");

        for (Role r : checkRoles) {
            if (Objects.equals(r.name(), currentUser.getRole().name())) {
                if (checkObj.getClass() == User.class) {
                    var check = (User) checkObj;
                    if (!Objects.equals(check.getEmail(), currentUser.getEmail())) {
                        throw new ForbiddenException("Role availability exception: can edit only self elements");
                    }
                }
                if (checkObj.getClass() == UserDtoRequest.class) {
                    var check = (UserDtoRequest) checkObj;
                    if (!Objects.equals(check.getEmail(), currentUser.getEmail())) {
                        throw new ForbiddenException("Role availability exception: can edit only self elements");
                    }
                }
            }
        }
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

    public void canEditOnlySelfElementsComment(Long commentId, Role[] roles) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        String commentAuthorEmail = comment.getAuthor().getEmail();

        if (!isCurrentUserEmailModifyingCurrentElement(commentAuthorEmail, roles)) {
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
