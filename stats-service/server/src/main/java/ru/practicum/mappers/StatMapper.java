package ru.practicum.mappers;

import ru.practicum.StatDto;
import ru.practicum.models.Stat;

import java.util.List;

public class StatMapper {
    public static StatDto map(Stat stat) {
        return StatDto.builder()
                .app(stat.getApp())
                .uri(stat.getUri())
                .hits(stat.getHits()).build();
    }

    public static List<StatDto> mapList(List<Stat> stats) {
        return stats.stream()
                .map(StatMapper::map)
                .toList();
    }
}
