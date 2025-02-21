package ru.practicum.category.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
public class CategoryDto {
    @NotNull
    private Long id;

    @NotNull
    @Length(min = 1, max = 50)
    private String name;
}
