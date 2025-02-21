package ru.practicum.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorController {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundException.class})
    public ErrorResponse handleNotFoundExceptions(final Exception ex) {
        log.error("Не найдено: {}", ex.getMessage(), ex);
        return new ErrorResponse("Не найдено: " + ex.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({ConflictException.class})
    public ErrorResponse handleConflictExceptions(final Exception ex) {
        log.error("Конфликт: {}", ex.getMessage(), ex);
        return new ErrorResponse("Конфликт: " + ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BadRequestException.class})
    public ErrorResponse handleBadRequestExceptions(final Exception ex) {
        log.error("Ошибка запроса: {}", ex.getMessage(), ex);
        return new ErrorResponse("Ошибка запроса: " + ex.getMessage());
    }
}
