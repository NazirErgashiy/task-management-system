package uz.nazir.task.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.nazir.task.entities.enums.Priority;
import uz.nazir.task.entities.enums.Status;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDtoResponse {
    private Long id;
    private String header;
    private String description;
    private Status status;
    private Priority priority;
    private Long taskAuthorId;
    private Long taskPerformerId;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
