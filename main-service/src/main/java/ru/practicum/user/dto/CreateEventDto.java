package ru.practicum.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateEventDto {
    @NotNull(message = "Не может быть пустым")
    @NotBlank(message = "Не может содержать пробелы")
    @Length(min = 20, max = 2000, message = "Не может быть короче 20 символов и длиннее 2000 символов")
    private String annotation;

    @NotNull(message = "Не может быть пустым")
    private Long category;

    @NotNull(message = "Не может быть пустым")
    @NotBlank(message = "Не может содержать пробелы")
    @Length(min = 20, max = 7000, message = "Не может быть короче 20 символов и длиннее 7000 символов")
    private String description;

    @NotNull(message = "Не может быть пустым")
    private String eventDate;

    @NotNull(message = "Не может быть пустым")
    private LocationDto location;

    private boolean paid;

    @PositiveOrZero(message = "Не может быть меньше 0")
    private int participantLimit = 0;

    private boolean requestModeration = true;

    @Length(min = 3, max = 120, message = "Не может быть короче 3 символов и длиннее 120 символов")
    private String title;
}
