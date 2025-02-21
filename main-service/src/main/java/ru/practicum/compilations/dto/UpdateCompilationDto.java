package ru.practicum.compilations.dto;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Builder
@Getter
public class UpdateCompilationDto {
    @Length(min = 1, max = 50)
    private String title;

    private boolean pinned;

    private List<Long> events;
}
