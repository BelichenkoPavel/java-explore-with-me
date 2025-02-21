package ru.practicum.events.services;

import com.querydsl.core.BooleanBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.CreateHitDto;
import ru.practicum.MainClient;
import ru.practicum.StatDto;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.mappers.CategoryMapper;
import ru.practicum.category.services.CategoryService;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.user.dto.*;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.user.mappers.LocationMapper;
import ru.practicum.user.mappers.UserMapper;
import ru.practicum.events.models.EventModel;
import ru.practicum.events.models.QEventModel;
import ru.practicum.user.models.UserModel;
import ru.practicum.events.repository.EventsRepository;
import ru.practicum.user.services.LocationService;
import ru.practicum.user.services.UserService;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    private final UserService userService;
    private final EventsRepository eventRepository;
    private final CategoryService categoryService;
    private final LocationService locationService;

    @Value("${stat.url}")
    private String statEndpoint;

    @Transactional
    public EventDto createEvent(Long userId, CreateEventDto eventDto) {
        LocalDateTime eventDate = LocalDateTime.parse(eventDto.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if (eventDate.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Event date must be in the future");
        }

        CategoryDto categoryDto = categoryService.getCategory(eventDto.getCategory());
        UserDto userDto = userService.getById(userId);
        LocationDto locationDto = locationService.createLocation(eventDto.getLocation());

        EventModel model = EventMapper.mapCreate(eventDto, categoryDto, userDto, locationDto);

        EventModel eventModel = eventRepository.save(model);

        return EventMapper.map(eventModel);
    }

    @Transactional
    public EventDto updateEventForUser(Long userId, Long eventId, UpdateEventDto eventDto) {
        UserDto initiator = userService.getById(userId);
        UserModel initiatorModel = UserMapper.map(initiator);

        Optional<EventModel> existEventModelOp = eventRepository.findAllByInitiatorAndId(initiatorModel, eventId);

        if (existEventModelOp.isEmpty()) {
            throw new NotFoundException("Event not found");
        }

        EventModel eventModel = existEventModelOp.get();

        if (eventModel.getState() == State.PUBLISHED) {
            throw new ConflictException("Event is published");
        }

        return updateEvent(eventModel, eventDto);
    }

    @Transactional
    public EventDto updateEventForAdmin(Long eventId, UpdateEventDto eventDto) {
        Optional<EventModel> existEventModelOp = eventRepository.findById(eventId);

        if (existEventModelOp.isEmpty()) {
            throw new NotFoundException("Event not found");
        }

        return updateEvent(existEventModelOp.get(), eventDto);
    }

    @Transactional
    public EventDto updateEvent(EventModel existEventModel, UpdateEventDto eventDto) {
        if (eventDto.getParticipantLimit() < 0) {
            throw new BadRequestException("participant limit must be positive");
        }

        if (eventDto.getEventDate() != null) {
            LocalDateTime eventDate = LocalDateTime.parse(eventDto.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            if (eventDate.isBefore(LocalDateTime.now())) {
                throw new BadRequestException("Event date must be in the future");
            }

            existEventModel.setEventDate(eventDate);
        }

        if (eventDto.getCategory() != null) {
            CategoryDto categoryDto = categoryService.getCategory(eventDto.getCategory());
            existEventModel.setCategory(CategoryMapper.map(categoryDto));
        }

        if (eventDto.getLocation() != null) {
            LocationDto locationDto = locationService.createLocation(eventDto.getLocation());
            existEventModel.setLocation(LocationMapper.map(locationDto));
        }

        if (eventDto.getAnnotation() != null) {
            existEventModel.setAnnotation(eventDto.getAnnotation());
        }

        if (eventDto.getDescription() != null) {
            existEventModel.setDescription(eventDto.getDescription());
        }

        if (eventDto.getPaid() != null) {
            existEventModel.setPaid(eventDto.getPaid());
        }

        if (eventDto.getRequestModeration() != null) {
            existEventModel.setRequestModeration(!existEventModel.isRequestModeration());
        }

        if (eventDto.getParticipantLimit() != 0) {
            existEventModel.setParticipantLimit(eventDto.getParticipantLimit());
        }

        if (eventDto.getTitle() != null) {
            existEventModel.setTitle(eventDto.getTitle());
        }

        StateAction newState = eventDto.getStateAction();
        if (newState != null) {
            if (newState.equals(StateAction.SEND_TO_REVIEW)) {
                existEventModel.setState(State.PENDING);
            } else if (newState.equals(StateAction.PUBLISH_EVENT)) {
                if (existEventModel.getState() == State.PUBLISHED) {
                    throw new ConflictException("Event already published");
                }

                if (existEventModel.getState() == State.CANCELED) {
                    throw new ConflictException("Event already canceled");
                }

                existEventModel.setState(State.PUBLISHED);
            } else if (newState.equals(StateAction.REJECT_EVENT)) {
                if (existEventModel.getState() == State.PUBLISHED) {
                    throw new ConflictException("Event already published");
                }

                existEventModel.setState(State.CANCELED);
            } else if (newState.equals(StateAction.CANCEL_REVIEW)) {
                existEventModel.setState(State.CANCELED);
            }
        }

        EventModel eventModel = eventRepository.save(existEventModel);

        return EventMapper.map(eventModel);
    }

    @Transactional
    public void setConfirmedRequests(EventDto eventDto) {
        EventModel eventModel = EventMapper.map(eventDto);

        eventRepository.save(eventModel);
    }

    public List<EventDto> getListByIds(List<Long> ids) {
        List<EventModel> list = eventRepository.findAllByIdIn(ids);

        return EventMapper.mapList(list);
    }

    public List<EventDto> getEvents(Long userId, Integer from, Integer size) {
        PageRequest pagination = PageRequest.of(from / size, size);
        UserDto initiator = userService.getById(userId);
        UserModel initiatorModel = UserMapper.map(initiator);

        List<EventModel> list = eventRepository.findAllByInitiator(initiatorModel, pagination);

        return EventMapper.mapList(list);
    }

    public EventDto getUserEvent(Long userId, Long eventId) {
        UserDto initiator = userService.getById(userId);
        UserModel initiatorModel = UserMapper.map(initiator);
        Optional<EventModel> model = eventRepository.findAllByInitiatorAndId(initiatorModel, eventId);

        if (model.isEmpty()) {
            throw new NotFoundException("Event not found");
        }

        return EventMapper.map(model.get());
    }

    public EventDto getEventPublic(Long eventId, HttpServletRequest request) {
        Optional<EventModel> modelOp = eventRepository.findById(eventId);

        if (modelOp.isEmpty()) {
            throw new NotFoundException("Event not found");
        }

        EventModel model = modelOp.get();

        if (model.getState() != State.PUBLISHED) {
            throw new NotFoundException("Event not published");
        }

        eventRepository.save(model);

        if (request != null) {
            sendHit(request);
            updateViews(List.of(model));
        }

        return EventMapper.map(model);
    }

    public List<EventDto> getEventsUser(String text, List<Long> categories,
                                        Boolean paid, String rangeStart,
                                        String rangeEnd, Boolean onlyAvailable,
                                        String sortStr,
                                        int from, int size,
                                        List<Long> users, List<State> states,
                                        HttpServletRequest request) {
        if (states == null) {
            states = List.of(State.PUBLISHED);
        } else {
            states.add(State.PUBLISHED);
        }

        List<EventDto> list = getEventsFull(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sortStr, from, size, users, states);

        sendHit(request);

        return list;
    }

    public List<EventDto> getEventsFull(String text,
                                        List<Long> categories,
                                        Boolean paid,
                                        String rangeStart,
                                        String rangeEnd,
                                        Boolean onlyAvailable,
                                        String sortStr,
                                        int from, int size,
                                        List<Long> users, List<State> states) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");

        if (sortStr.equals("EVENT_DATE")) {
            sort = Sort.by(Sort.Direction.DESC, "eventDate");
        } else if (sortStr.equals("VIEWS")) {
            sort = Sort.by(Sort.Direction.DESC, "views");
        }

        BooleanBuilder builder = new BooleanBuilder();

        if (text != null && !text.isBlank()) {
            String s = "%" + text.trim() + "%";
            builder
                .and(QEventModel.eventModel.annotation.likeIgnoreCase(s))
                .or(QEventModel.eventModel.description.likeIgnoreCase(s));
        }

        if (categories != null && categories.getFirst() != 0L) {
            builder.and(QEventModel.eventModel.category.id.in(categories));
        }

        if (onlyAvailable != null && onlyAvailable) {
            builder.and(QEventModel.eventModel.confirmedRequests.eq(QEventModel.eventModel.participantLimit));
        }

        if (paid != null) {
            builder.and(QEventModel.eventModel.paid.eq(paid));
        }

        if (rangeStart != null) {
            LocalDateTime date = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            builder.and(QEventModel.eventModel.eventDate.after(date));

        }

        if (rangeEnd != null) {
            LocalDateTime date = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            builder.and(QEventModel.eventModel.eventDate.before(date));
        }

        if (users != null) {
            builder.and(QEventModel.eventModel.initiator.id.in(users));
        }

        if (states != null) {
            builder.and(QEventModel.eventModel.state.in(states));
        }

        PageRequest pagination = PageRequest.of(from / size, size, sort);

        List<EventModel> list;

        if (builder.getValue() != null) {
            list = eventRepository.findAll(builder.getValue(), pagination).getContent();
        } else {
            list = eventRepository.findAll(pagination).getContent();
        }

        if (list.isEmpty()) {
            throw new BadRequestException("Events not found");
        }

        return EventMapper.mapList(list);
    }

    public List<EventDto> findByCategory(Long categoryId) {
        List<EventModel> list = eventRepository.findAllByCategoryId(categoryId);

        return EventMapper.mapList(list);
    }

    @Transactional
    private void updateViews(List<EventModel> models) {
        MainClient client = new MainClient(this.statEndpoint, new RestTemplateBuilder());

        try {
            String[] uri = models.stream()
                    .map(event -> "/events/" + event.getId())
                    .toArray(String[]::new);

            List<StatDto> list = client.getStats(
                    LocalDateTime.now().minusYears(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    uri,
                    true);

            Map<String, Integer> uriHitMap = list.stream()
                    .collect(Collectors.toMap(StatDto::getUri, StatDto::getHits));
            for (EventModel event : models) {
                event.setViews(uriHitMap.getOrDefault("/events/" + event.getId(), 0));
                eventRepository.save(event);
            }
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    private void sendHit(HttpServletRequest request) {
        MainClient client = new MainClient(this.statEndpoint, new RestTemplateBuilder());

        try {
            CreateHitDto endpointHitDto = CreateHitDto
                    .builder()
                    .ip(request.getRemoteAddr())
                    .app("main-service")
                    .uri(request.getRequestURI())
                    .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .build();

            client.addHit(endpointHitDto);

        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
