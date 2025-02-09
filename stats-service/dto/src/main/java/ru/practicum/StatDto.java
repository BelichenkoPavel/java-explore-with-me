package ru.practicum;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StatDto {
    @NotNull(message = "app is required")
    private String app;

    @NotNull(message = "uri is required")
    private String uri;

    @NotNull(message = "hits is required")
    private int hits;
}
