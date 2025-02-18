package ru.practicum.user.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventDto {
    @Length(min = 20, max = 2000, message = "Не может быть короче 20 символов и длиннее 2000 символов")
    private String annotation;

    private Long category;

    @Length(min = 20, max = 7000, message = "Не может быть короче 20 символов и длиннее 7000 символов")
    private String description;

    private String eventDate;

    private LocationDto location;

    private Boolean paid;

    private int participantLimit;

    private Boolean requestModeration;

    private StateAction stateAction;

    @Length(min = 3, max = 120, message = "Не может быть короче 3 символов и длиннее 120 символов")
    private String title;
}
