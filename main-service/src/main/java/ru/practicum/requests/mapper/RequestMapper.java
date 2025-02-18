package ru.practicum.requests.mapper;

import ru.practicum.requests.dto.RequestDto;
import ru.practicum.requests.models.RequestModel;
import ru.practicum.events.dto.EventDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.user.mappers.UserMapper;
import ru.practicum.events.models.EventModel;
import ru.practicum.user.models.UserModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RequestMapper {
    public static RequestModel createMap(EventDto event, UserDto userDto) {
        EventModel eventModel = EventMapper.map(event);
        UserModel userModel = UserMapper.map(userDto);

        String status = "PENDING";

        if (event.getParticipantLimit() == 0) {
            status = "CONFIRMED";
        }

        if (!event.isRequestModeration()) {
            status = "CONFIRMED";
        }

        return RequestModel.builder()
                .event(eventModel)
                .requester(userModel)
                .created(LocalDateTime.now())
                .status(status)
                .build();
    }

    public static RequestDto map(RequestModel request) {
        return RequestDto.builder()
                .id(request.getId())
                .created(request.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .status(request.getStatus())
                .requester(request.getRequester().getId())
                .event(request.getEvent().getId())
                .build();

    }

    public static List<RequestDto> mapList(List<RequestModel> requestModels) {
        return requestModels.stream()
                .map(RequestMapper::map)
                .toList();
    }
}
