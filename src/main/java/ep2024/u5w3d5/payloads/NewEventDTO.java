package ep2024.u5w3d5.payloads;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record NewEventDTO(
        @NotBlank(message = "Title must not be empty!")
        @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters.")
        String title,

        @NotBlank(message = "Description must not be empty!")
        @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters.")
        String description,

        @NotNull(message = "Date must not be null!")
        @Future(message = "Date must be in the future!")
        LocalDate date,

        @NotBlank(message = "Location must not be empty!")
        @Size(min = 3, max = 100, message = "Location must be between 3 and 100 characters.")
        String location,

        @NotNull(message = "Available seats must not be null!")
        Integer availableSeats,

        @NotBlank(message = "Organizer ID must not be empty!")
        String organizerId
) {
}
