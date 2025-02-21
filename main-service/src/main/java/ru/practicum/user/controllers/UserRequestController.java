package ru.practicum.user.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.requests.dto.RequestDto;
import ru.practicum.requests.services.RequestService;
import ru.practicum.user.dto.UpdateUserRequestsDto;
import ru.practicum.user.dto.UpdateUserRequestsResultDto;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
@Validated
public class UserRequestController {
    private final RequestService requestService;

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<RequestDto> getUserRequests(
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        return requestService.getUserRequests(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public UpdateUserRequestsResultDto updateUserRequests(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody UpdateUserRequestsDto requests) {
        return requestService.updateUserRequests(userId, eventId, requests);
    }

    @GetMapping("/{userId}/requests")
    public List<RequestDto> getRequests(@PathVariable Long userId) {
        return requestService.getRequests(userId);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto create(
            @PathVariable Long userId,
            @RequestParam @Valid Long eventId) {
        return requestService.addRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public RequestDto cancelRequest(
            @PathVariable Long userId,
            @PathVariable Long requestId) {
        return requestService.cancelRequest(userId, requestId);
    }
}
