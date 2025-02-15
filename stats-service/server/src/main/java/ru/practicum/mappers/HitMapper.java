package ru.practicum.mappers;

import ru.practicum.CreateHitDto;
import ru.practicum.models.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HitMapper {
    public static Hit mapCreate(CreateHitDto dto) {
        LocalDateTime timestamp = LocalDateTime.parse(dto.getTimestamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return Hit.builder()
                .app(dto.getApp())
                .uri(dto.getUri())
                .ip(dto.getIp())
                .timestamp(timestamp).build();
    }
}
