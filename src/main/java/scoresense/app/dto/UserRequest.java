package scoresense.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequest {

    @NotBlank(message = "The username is required")
    @Size(max = 50)
    private String username;

    @NotBlank(message = "The email is required")
    @Email(message = "Must be a valid email")
    @Size(max = 100)
    private String email;

    @NotBlank(message = "The password is required")
    @Size(min = 6, max = 255, message = "The password must be at least 6 characters long")

    private String password;

    private Long roleId;
}
