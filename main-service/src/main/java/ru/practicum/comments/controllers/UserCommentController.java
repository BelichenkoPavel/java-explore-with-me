package ru.practicum.comments.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CreateCommentDto;
import ru.practicum.comments.dto.UpdateCommentDto;
import ru.practicum.comments.services.CommentsService;

import java.util.List;

@RestController
@RequestMapping("/comments")
@AllArgsConstructor
public class UserCommentController {
    private final CommentsService commentsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CommentDto createComment(@Valid @RequestBody CreateCommentDto commentDto) {
        return commentsService.createComment(commentDto);
    }

    @PatchMapping
    CommentDto updateComment(@Valid @RequestBody UpdateCommentDto commentDto) {
        return commentsService.updateComment(commentDto);
    }

    @GetMapping("/{commentId}")
    CommentDto getComment(@RequestHeader("X-User-Id") Long userId,
                          @PathVariable Long commentId) {
        return commentsService.getCommentPublic(userId, commentId);
    }

    @GetMapping("/events/{eventId}")
    List<CommentDto> getAllComments(@PathVariable Long eventId) {
        return commentsService.getAllComments(eventId);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteComment(@RequestHeader("X-User-Id") Long userId,
                       @PathVariable Long commentId) {
        commentsService.deleteComment(userId, commentId);
    }
}
