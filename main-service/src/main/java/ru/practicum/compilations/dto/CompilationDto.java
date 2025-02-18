package ru.practicum.compilations.dto;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.events.dto.EventDto;

import java.util.List;

@Builder
@Getter
public class CompilationDto {
    private Long id;

    private String title;

    private boolean pinned;

    private List<EventDto> events;
}
