package ru.practicum.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.requests.dto.RequestDto;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequestsResultDto {
    private List<RequestDto> confirmedRequests;

    private List<RequestDto> rejectedRequests;
}
