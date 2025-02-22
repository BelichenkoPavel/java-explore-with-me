package ru.practicum.comments.dto;

import lombok.*;
import ru.practicum.events.dto.EventDto;
import ru.practicum.user.dto.UserDto;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;

    private String text;

    private CommentState status;

    private EventDto event;

    private UserDto user;

    private String createDate;
}
