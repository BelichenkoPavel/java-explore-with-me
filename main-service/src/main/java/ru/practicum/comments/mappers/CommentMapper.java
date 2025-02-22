package ru.practicum.comments.mappers;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentState;
import ru.practicum.comments.dto.CreateCommentDto;
import ru.practicum.comments.dto.UpdateCommentDto;
import ru.practicum.comments.models.CommentModel;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.models.EventModel;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mappers.UserMapper;
import ru.practicum.user.models.UserModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CommentMapper {
    public static CommentModel createMap(CreateCommentDto dto, UserDto userDto, EventDto eventDto) {
        UserModel user = UserMapper.map(userDto);
        EventModel event = EventMapper.map(eventDto);

        return CommentModel.builder()
                .text(dto.getText())
                .status(CommentState.PENDING)
                .user(user)
                .event(event)
                .createDate(LocalDateTime.now())
                .build();
    }

    public static CommentModel updateMap(UpdateCommentDto dto, UserDto userDto, EventDto eventDto) {
        UserModel user = UserMapper.map(userDto);
        EventModel event = EventMapper.map(eventDto);

        return CommentModel.builder()
                .text(dto.getText())
                .status(CommentState.PENDING)
                .user(user)
                .event(event)
                .createDate(LocalDateTime.now())
                .build();
    }

    public static CommentDto map(CommentModel dto) {
        UserDto user = UserMapper.map(dto.getUser());
        EventDto event = EventMapper.map(dto.getEvent());

        return CommentDto.builder()
                .id(dto.getId())
                .text(dto.getText())
                .status(dto.getStatus())
                .user(user)
                .event(event)
                .createDate(dto.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    public static List<CommentDto> mapList(List<CommentModel> model) {
        return model.stream()
                .map(CommentMapper::map)
                .toList();
    }
}
