package ru.practicum.models;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Stat {
    @NotNull(message = "app is required")
    private String app;

    @NotNull(message = "uri is required")
    private String uri;

    @NotNull(message = "hits is required")
    private int hits;

    public Stat(String app, String uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = Math.toIntExact(hits);
    }
}
