package ru.practicum.requests.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.requests.dto.RequestDto;
import ru.practicum.requests.mapper.RequestMapper;
import ru.practicum.requests.models.RequestModel;
import ru.practicum.requests.repository.RequestRepository;
import ru.practicum.events.dto.EventDto;
import ru.practicum.user.dto.State;
import ru.practicum.user.dto.UpdateUserRequestsDto;
import ru.practicum.user.dto.UpdateUserRequestsResultDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.events.services.EventService;
import ru.practicum.user.services.UserService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RequestService {
    private final RequestRepository repository;
    private final UserService userService;
    private final EventService eventService;

    public RequestDto addRequest(Long userId, Long eventId) {
        UserDto userDto = userService.getById(userId);
        EventDto eventDto;

        try {
            eventDto = eventService.getEventPublic(eventId, null);
        } catch (NotFoundException e) {
            throw new ConflictException("Event not published");
        }

        if (eventDto.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Cannot add request for initiator");
        }

        if (eventDto.getState() != State.PUBLISHED) {
            throw new ConflictException("Event is not published");
        }

        List<RequestModel> list = repository.findAllByRequesterIdAndEventId(userId, eventId);

        if (!list.isEmpty()) {
            throw new ConflictException("Request already exists");
        }

        if (!eventDto.isRequestModeration()) {
            if (eventDto.getParticipantLimit() > 0 &&  eventDto.getConfirmedRequests() >= eventDto.getParticipantLimit()) {
                throw new ConflictException("Event is full");
            }

            eventDto.setConfirmedRequests(eventDto.getConfirmedRequests() + 1);
            eventService.setConfirmedRequests(eventDto);
        }

        RequestModel requestModel = RequestMapper.createMap(eventDto, userDto);

        RequestModel saveModel = repository.save(requestModel);

        return RequestMapper.map(saveModel);
    }

    public List<RequestDto> getRequests(Long userId) {
        userService.getById(userId);

        List<RequestModel> requestModels = repository.findAllByRequesterId(userId);

        return RequestMapper.mapList(requestModels);
    }

    public List<RequestDto> getUserRequests(Long userId, Long eventId) {
        userService.getById(userId);
        List<RequestModel> requestModels = repository.findAllByEventId(eventId);

        return RequestMapper.mapList(requestModels);
    }

    public UpdateUserRequestsResultDto updateUserRequests(Long userId, Long eventId, UpdateUserRequestsDto requests) {
        userService.getById(userId);
        EventDto eventDto = eventService.getUserEvent(userId, eventId);

        List<RequestModel> requestModels = repository.findAllByIdIn(requests.getRequestIds());

        List<RequestDto> confirmedRequests = new java.util.ArrayList<>();
        List<RequestDto> rejectedRequests = new java.util.ArrayList<>();

        if (eventDto.getParticipantLimit() > 0 && eventDto.getConfirmedRequests() >= eventDto.getParticipantLimit()) {
            throw new ConflictException("Event is full");
        }

        requestModels.forEach(model -> {
            if (requests.getStatus().equals("CONFIRMED")) {
                if (eventDto.getConfirmedRequests() < eventDto.getParticipantLimit()) {
                    eventDto.setConfirmedRequests(eventDto.getConfirmedRequests() + 1);
                    model.setStatus("CONFIRMED");
                    confirmedRequests.add(RequestMapper.map(model));
                }
            } else {
                if (model.getStatus().equals("CONFIRMED")) {
                    throw new ConflictException("Request already confirmed");
                }

                model.setStatus("REJECTED");
                rejectedRequests.add(RequestMapper.map(model));
            }

            repository.save(model);
        });

        eventService.setConfirmedRequests(eventDto);

        return new UpdateUserRequestsResultDto(confirmedRequests, rejectedRequests);
    }

    public RequestDto cancelRequest(Long userId, Long requestId) {
        userService.getById(userId);

        Optional<RequestModel> modelOp = repository.findById(requestId);

        if (modelOp.isEmpty()) {
            throw new NotFoundException("Request not found");
        }

        RequestModel model = modelOp.get();

        model.setStatus("CANCELED");

        return RequestMapper.map(repository.save(model));
    }
}
