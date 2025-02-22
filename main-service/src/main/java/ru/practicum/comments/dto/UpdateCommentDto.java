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
public class UpdateCommentDto {
    @NotNull(message = "Comment text is required")
    @NotBlank(message = "Comment text must not be blank")
    private String text;

    @NotNull(message = "Comment id is required")
    private Long commentId;

    @NotNull(message = "user id is required")
    private Long userId;
}
