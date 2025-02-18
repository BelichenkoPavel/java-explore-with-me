package ru.practicum.user.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.services.EventService;
import ru.practicum.user.dto.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
@Validated
public class UserEventController {
    private final EventService eventService;

    @GetMapping("/{userId}/events")
    public List<EventResponseDto> getEvents(
            @PathVariable Long userId,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        List<EventDto> list = eventService.getEvents(userId, from, size);

        return EventMapper.mapClientList(list);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponseDto addEvent(@PathVariable Long userId, @Valid @RequestBody CreateEventDto eventDto) {
        EventDto dto = eventService.createEvent(userId, eventDto);

        return EventMapper.mapClient(dto);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventResponseDto updateEvent(@PathVariable Long userId, @PathVariable Long eventId, @Valid @RequestBody UpdateEventDto eventDto) {
        EventDto dto = eventService.updateEventForUser(userId, eventId, eventDto);

        return EventMapper.mapClient(dto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventDto getEvent(
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        return eventService.getUserEvent(userId, eventId);
    }
}