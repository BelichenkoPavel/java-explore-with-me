package ru.practicum.comments.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentDto {
    @NotNull(message = "text is required")
    @NotBlank(message = "text must not be blank")
    private String text;

    @NotNull(message = "event id is required")
    private Long eventId;

    @NotNull(message = "user id is required")
    private Long userId;
}
