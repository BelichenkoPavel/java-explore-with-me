package ru.practicum.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LocationDto {
    private Long id;

    private Float lat;

    private Float lon;
}
