package ru.practicum.user.dto;

import lombok.*;
import ru.practicum.category.dto.CategoryDto;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventResponseDto {
    private Long id;

    private String annotation;

    private int confirmedRequests;

    private CategoryDto category;

    private LocalDateTime createdOn;

    private String description;

    private String eventDate;

    private UserDto initiator;

    private LocationDto location;

    private boolean paid;

    private int participantLimit;

    private LocalDateTime publishedOn;

    private boolean requestModeration;

    private State state;

    private String title;

    private int views;
}
