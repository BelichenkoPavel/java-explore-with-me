package ru.practicum.events.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.services.EventService;
import ru.practicum.user.dto.EventResponseDto;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Validated
public class EventController {
    private final EventService eventService;

    @GetMapping
    public List<EventResponseDto> getEvents(
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "from", required = false) List<Long> categories,
            @RequestParam(name = "paid", required = false) Boolean paid,
            @RequestParam(name = "rangeStart", required = false) String rangeStart,
            @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(name = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(name = "sort", defaultValue = "") String sort,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        List<EventDto> eventDto = eventService.getEventsUser(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, null, null, request);

        return EventMapper.mapClientList(eventDto);
    }

    @GetMapping("/{userId}")
    public EventResponseDto getEvent(@PathVariable Long userId,
                                     HttpServletRequest request) {
        EventDto eventDto = eventService.getEventPublic(userId, request);

        return EventMapper.mapClient(eventDto);
    }
}
