package ru.practicum.compilations.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Builder
@Getter
public class CreateCompilationDto {
    @NotNull(message = "title is required")
    @NotBlank(message = "title is required")
    @Length(min = 1, max = 50)
    private String title;

    private boolean pinned;

    private List<Long> events;
}
