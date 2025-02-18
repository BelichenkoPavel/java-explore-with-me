package ru.practicum.compilations.mappers;

import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.CreateCompilationDto;
import ru.practicum.compilations.models.CompilationModel;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.mapper.EventMapper;

import java.util.List;

public class CompilationMapper {
    public static CompilationModel mapCreate(CreateCompilationDto compilationDto) {
        return CompilationModel.builder()
                .pinned(compilationDto.isPinned())
                .title(compilationDto.getTitle())
                .build();
    }

    public static CompilationDto map(CompilationModel compilation) {
        List<EventDto> eventDtoList;

        if (compilation.getEvents() == null) {
            eventDtoList = List.of();
        } else {
            eventDtoList = EventMapper.mapList(compilation.getEvents().stream().toList());
        }

        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.isPinned())
                .events(eventDtoList)
                .build();
    }

    public static CompilationDto map(CompilationModel compilation, List<EventDto> dtoList) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.isPinned())
                .events(dtoList)
                .build();
    }

    public static List<CompilationDto> mapList(List<CompilationModel> list) {
        return list.stream()
                .map(CompilationMapper::map)
                .toList();
    }
}
