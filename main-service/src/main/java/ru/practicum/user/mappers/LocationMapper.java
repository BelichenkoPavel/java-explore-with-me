package ru.practicum.user.mappers;

import lombok.experimental.UtilityClass;
import ru.practicum.user.dto.LocationDto;
import ru.practicum.user.models.LocationModel;

@UtilityClass
public class LocationMapper {
    public static LocationDto map(LocationModel model) {
        return LocationDto.builder()
                .id(model.getId())
                .lat(model.getLat())
                .lon(model.getLon())
                .build();
    }

    public static LocationModel map(LocationDto model) {
        return LocationModel.builder()
                .id(model.getId())
                .lat(model.getLat())
                .lon(model.getLon())
                .build();
    }
}
