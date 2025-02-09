package ru.practicum.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.CreateHitDto;
import ru.practicum.StatDto;
import ru.practicum.mappers.HitMapper;
import ru.practicum.mappers.StatMapper;
import ru.practicum.models.Hit;
import ru.practicum.models.Stat;
import ru.practicum.repository.IHitDBRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class StatService {
    private final IHitDBRepository repository;

    public List<StatDto> getStats(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        List<Stat> hits;

        if (uris != null) {
            if (unique) {
                hits = repository.findByUriInUnique(uris, start, end);
            } else {
                hits = repository.findGroupByUriIn(uris, start, end);
            }
        } else {
            hits = repository.findGroupByUri(start, end);
        }

        return StatMapper.mapList(hits);
    }

    public void createHit(CreateHitDto dto) {
        Hit hit = HitMapper.mapCreate(dto);

        repository.save(hit);
    }
}
