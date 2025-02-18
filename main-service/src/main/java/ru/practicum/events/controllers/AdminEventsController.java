package ru.practicum.events.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.services.EventService;
import ru.practicum.user.dto.EventResponseDto;
import ru.practicum.user.dto.State;
import ru.practicum.user.dto.UpdateEventDto;

import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Validated
public class AdminEventsController {
    private final EventService eventService;

    @GetMapping
    public List<EventResponseDto> getEvents(
            @RequestParam(name = "users", required = false) List<Long> users,
            @RequestParam(name = "states", required = false) List<State> states,
            @RequestParam(name = "categories", required = false) List<Long> categories,
            @RequestParam(name = "rangeStart", required = false) String rangeStart,
            @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        List<EventDto> list = eventService.getEventsFull("", categories, null, rangeStart, rangeEnd, null, "", from, size, users, states);

        return EventMapper.mapClientList(list);
    }

    @PatchMapping("/{eventId}")
    public EventResponseDto updateEvent(@PathVariable Long eventId, @Valid @RequestBody UpdateEventDto eventDto) {
        EventDto dto = eventService.updateEventForAdmin(eventId, eventDto);

        return EventMapper.mapClient(dto);
    }
}
