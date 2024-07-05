package ep2024.u5w3d5.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewUserDTO(
        @NotBlank(message = "Name must not be empty!")
        @Size(min = 3, max = 30, message = "Name must be between 3 and 30 characters.")
        String name,

        @NotBlank(message = "Email must not be empty!")
        @Email(message = "Email should be valid")
        String email,

        @NotBlank(message = "Password must not be empty!")
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.")
        String password,

        @NotBlank(message = "Role must not be empty!")
        String role
) {
}
