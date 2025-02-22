package ru.practicum.comments.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentState;
import ru.practicum.comments.dto.ModerateCommentDto;
import ru.practicum.comments.services.CommentsService;

import java.util.List;

@RestController
@RequestMapping("/admin/comments")
@AllArgsConstructor
public class AdminCommentsController {
    private final CommentsService commentsService;

    @PatchMapping("/{commentId}")
    CommentDto moderateComment(@PathVariable Long commentId,
                               @Valid @RequestBody ModerateCommentDto commentDto) {
        return commentsService.moderateComment(commentId, commentDto);
    }

    @GetMapping
    List<CommentDto> getAllComments(
            @RequestParam(name = "events", required = false) List<Long> events,
            @RequestParam(name = "status", required = false) List<CommentState> status,
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "rangeStart", required = false) String rangeStart,
            @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        return commentsService.getAllCommentsAdmin(events, status, text, rangeStart, rangeEnd, from, size);
    }
}
