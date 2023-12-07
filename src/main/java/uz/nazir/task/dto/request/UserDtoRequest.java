package uz.nazir.task.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.nazir.task.validators.annotation.Enum;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoRequest {

    @Size(min = 3, max = 15, message = "Name size limits [3-15]")
    private String name;

    @Email
    private String email;

    @Enum("USER")
    private String role;
}
