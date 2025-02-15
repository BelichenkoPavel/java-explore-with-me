package ru.practicum;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateHitDto {
    @NotNull(message = "app is required")
    private String app;

    @NotNull(message = "uri is required")
    private String uri;

    @NotNull(message = "ip is required")
    private String ip;

    @NotNull(message = "timestamp is required")
    private String timestamp;
}
