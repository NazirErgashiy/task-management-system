package uz.nazir.task.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDtoRequest {

    @Size(min = 1, max = 255, message = "Description size limits [1-255]")
    private String description;

    @Min(1)
    private Long taskId;

    @Null
    private Long authorId;
}
