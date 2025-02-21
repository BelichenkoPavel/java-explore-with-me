package ru.practicum.category.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCategoryDto {
    @NotNull
    @Length(min = 1, max = 50)
    private String name;
}
