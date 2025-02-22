package ru.practicum.comments.services;

import com.querydsl.core.BooleanBuilder;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comments.dto.*;
import ru.practicum.comments.mappers.CommentMapper;
import ru.practicum.comments.models.CommentModel;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.events.dto.EventDto;
import ru.practicum.comments.models.QCommentModel;
import ru.practicum.events.services.EventService;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.services.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentsService {
    private final CommentRepository commentRepository;

    private final EventService eventsService;

    private final UserService userService;

    @Transactional
    public CommentDto createComment(CreateCommentDto commentDto) {
        UserDto userDto = userService.getById(commentDto.getUserId());

        EventDto eventDto = eventsService.getEventPublic(commentDto.getEventId(), null);

        CommentModel model = CommentMapper.createMap(commentDto, userDto, eventDto);
        CommentModel saveModel = commentRepository.save(model);
        
        return CommentMapper.map(saveModel);
    }

    @Transactional
    public CommentDto updateComment(UpdateCommentDto commentDto) {
        userService.getById(commentDto.getUserId());

        CommentModel model = getCommentModel(commentDto.getUserId(), commentDto.getCommentId());

        if (model.getStatus() == CommentState.PENDING) {
            throw new ConflictException("Comment pending");
        }

        if (model.getStatus() == CommentState.PUBLISHED) {
            throw new ConflictException("Comment already published");
        }

        if (!Objects.equals(commentDto.getText(), model.getText())) {
            model.setText(commentDto.getText());
        }

        CommentModel saveModel = commentRepository.save(model);

        return CommentMapper.map(saveModel);
    }

    public CommentDto getCommentPublic(Long userId, Long commentId) {
        userService.getById(userId);

        return getComment(userId, commentId);
    }

    public CommentDto getComment(Long userId, Long commentId) {
        CommentModel model = getCommentModel(userId, commentId);

        return CommentMapper.map(model);
    }

    public CommentModel getCommentModel(Long userId, Long commentId) {
        Optional<CommentModel> model = commentRepository.findByIdAndUserId(commentId, userId);

        if (model.isEmpty()) {
            throw new NotFoundException("Comment not found");
        }

        return model.get();
    }

    public List<CommentDto> getAllComments(Long eventId) {
        List<CommentModel> models = commentRepository.findAllByEventIdAndStatus(eventId, CommentState.PUBLISHED);

        return CommentMapper.mapList(models);
    }

    public void deleteComment(Long userId, Long commentId) {
        userService.getById(userId);

        CommentModel model = getCommentModel(userId, commentId);

        commentRepository.delete(model);
    }

    public CommentDto moderateComment(Long commentId, ModerateCommentDto moderateCommentDto) {
        Optional<CommentModel> modelOpt = commentRepository.findById(commentId);

        if (modelOpt.isEmpty()) {
            throw new NotFoundException("Comment not found");
        }

        CommentModel model = modelOpt.get();

        if (model.getStatus() != CommentState.PENDING) {
            throw new ConflictException("Comment already reviewed");
        }

        if (moderateCommentDto.getStatus() == ModerateCommentState.REJECTED) {
            model.setStatus(CommentState.REJECTED);
        } else if (moderateCommentDto.getStatus() == ModerateCommentState.PUBLISHED) {
            model.setStatus(CommentState.PUBLISHED);
        }

        commentRepository.save(model);
        return CommentMapper.map(model);
    }

    public List<CommentDto> getAllCommentsAdmin(List<Long> events,
                                                List<CommentState> status,
                                                String text,
                                                String rangeStart,
                                                String rangeEnd,
                                                Integer from, Integer size) {
        BooleanBuilder builder = new BooleanBuilder();

        if (text != null && !text.isBlank()) {
            String s = "%" + text.trim() + "%";
            builder.and(QCommentModel.commentModel.text.likeIgnoreCase(s));
        }

        if (status != null) {
            builder.and(QCommentModel.commentModel.status.in(status));
        }

        if (events != null) {
            builder.and(QCommentModel.commentModel.event.id.in(events));
        }

        if (rangeStart != null) {
            LocalDateTime date = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            builder.and(QCommentModel.commentModel.createDate.after(date));
        }

        if (rangeEnd != null) {
            LocalDateTime date = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            builder.and(QCommentModel.commentModel.createDate.before(date));
        }

        PageRequest pagination = PageRequest.of(from / size, size);

        List<CommentModel> list;

        if (builder.getValue() != null) {
            list = commentRepository.findAll(builder.getValue(), pagination).getContent();
        } else {
            list = commentRepository.findAll(pagination).getContent();
        }

        return CommentMapper.mapList(list);
    }
}
