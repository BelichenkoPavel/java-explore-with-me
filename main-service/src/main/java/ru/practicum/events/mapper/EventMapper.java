package ru.practicum.events.mapper;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.mappers.CategoryMapper;
import ru.practicum.category.models.CategoryModel;
import ru.practicum.events.dto.EventDto;
import ru.practicum.user.dto.*;
import ru.practicum.user.mappers.LocationMapper;
import ru.practicum.user.mappers.UserMapper;
import ru.practicum.events.models.EventModel;
import ru.practicum.user.models.LocationModel;
import ru.practicum.user.models.UserModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EventMapper {
    public static EventModel mapCreate(CreateEventDto model,
                                       CategoryDto categoryDto,
                                       UserDto initiatorDto,
                                       LocationDto locationDto) {
        LocalDateTime eventDate = LocalDateTime.parse(model.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        CategoryModel categoryModel = CategoryMapper.map(categoryDto);
        UserModel userModel = UserMapper.map(initiatorDto);
        LocationModel locationModel = LocationMapper.map(locationDto);

        return EventModel.builder()
                .annotation(model.getAnnotation())
                .category(categoryModel)
                .createdOn(LocalDateTime.now())
                .description(model.getDescription())
                .eventDate(eventDate)
                .initiator(userModel)
                .location(locationModel)
                .paid(model.isPaid())
                .state(State.PENDING)
                .views(0)
                .participantLimit(model.getParticipantLimit())
                .requestModeration(model.isRequestModeration())
                .title(model.getTitle())
                .build();
    }

    public static EventModel map(EventDto model) {
        CategoryModel category = CategoryMapper.map(model.getCategory());
        UserModel initiator = UserMapper.map(model.getInitiator());
        LocationModel location = LocationMapper.map(model.getLocation());

        return EventModel.builder()
                .id(model.getId())
                .annotation(model.getAnnotation())
                .confirmedRequests(model.getConfirmedRequests())
                .category(category)
                .createdOn(model.getCreatedOn())
                .description(model.getDescription())
                .eventDate(model.getEventDate())
                .initiator(initiator)
                .location(location)
                .paid(model.isPaid())
                .participantLimit(model.getParticipantLimit())
                .publishedOn(model.getPublishedOn())
                .requestModeration(model.isRequestModeration())
                .state(model.getState())
                .title(model.getTitle())
                .views(model.getViews())
                .build();
    }

    public static EventDto map(EventModel model) {
        CategoryDto categoryDto = CategoryMapper.map(model.getCategory());
        UserDto initiatorDto = UserMapper.map(model.getInitiator());
        LocationDto locationDto = LocationMapper.map(model.getLocation());

        return EventDto.builder()
                .id(model.getId())
                .annotation(model.getAnnotation())
                .confirmedRequests(model.getConfirmedRequests())
                .category(categoryDto)
                .createdOn(model.getCreatedOn())
                .description(model.getDescription())
                .eventDate(model.getEventDate())
                .initiator(initiatorDto)
                .location(locationDto)
                .paid(model.isPaid())
                .participantLimit(model.getParticipantLimit())
                .publishedOn(model.getPublishedOn())
                .requestModeration(model.isRequestModeration())
                .state(model.getState())
                .title(model.getTitle())
                .views(model.getViews())
                .build();
    }

    public static EventResponseDto mapClient(EventDto model) {
        String eventDate = model.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return EventResponseDto.builder()
                .id(model.getId())
                .annotation(model.getAnnotation())
                .confirmedRequests(model.getConfirmedRequests())
                .category(model.getCategory())
                .createdOn(model.getCreatedOn())
                .description(model.getDescription())
                .eventDate(eventDate)
                .initiator(model.getInitiator())
                .location(model.getLocation())
                .paid(model.isPaid())
                .participantLimit(model.getParticipantLimit())
                .publishedOn(model.getPublishedOn())
                .requestModeration(model.isRequestModeration())
                .state(model.getState())
                .title(model.getTitle())
                .views(model.getViews())
                .build();
    }

    public static List<EventDto> mapList(List<EventModel> dtoList) {
        return dtoList.stream()
                .map(EventMapper::map)
                .toList();
    }

    public static List<EventModel> mapModelList(List<EventDto> dtoList) {
        return dtoList.stream()
                .map(EventMapper::map)
                .toList();
    }

    public static List<EventResponseDto> mapClientList(List<EventDto> dtoList) {
        return dtoList.stream()
                .map(EventMapper::mapClient)
                .toList();
    }
}
