package ru.practicum.user.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.user.dto.LocationDto;
import ru.practicum.user.mappers.LocationMapper;
import ru.practicum.user.models.LocationModel;
import ru.practicum.user.repository.LocationRepository;

@Service
@AllArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    public LocationDto createLocation(LocationDto locationDto) {
        LocationModel model = LocationMapper.map(locationDto);
        LocationModel saved = locationRepository.save(model);

        return LocationMapper.map(saved);
    }
}
