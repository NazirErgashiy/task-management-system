package uz.nazir.task.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.nazir.task.validators.annotation.Enum;

import javax.validation.constraints.Min;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDtoRequest {

    @Size(min = 5, max = 30, message = "Header size limits [5-30]")
    private String header;

    @Size(min = 10, max = 255, message = "Description size limits [10-255]")
    private String description;

    @Enum(value = {"EXPECTATION", "STARTED", "COMPLETED"}, message = "Status available values [EXPECTATION, STARTED, COMPLETED]")
    private String status;

    @Enum(value = {"LOW", "MEDIUM", "HIGH"}, message = "Priority available values [LOW, MEDIUM, HIGH]")
    private String priority;

    @Null
    private Long taskAuthorId;

    @Min(1)
    private Long taskPerformerId;
}
