package uz.nazir.task.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.nazir.task.entities.enums.Role;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoResponse {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private List<Long> tasksAuthorId;
    private List<Long> tasksPerformerId;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
