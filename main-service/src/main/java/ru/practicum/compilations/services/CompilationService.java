package ru.practicum.compilations.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.CreateCompilationDto;
import ru.practicum.compilations.dto.UpdateCompilationDto;
import ru.practicum.compilations.mappers.CompilationMapper;
import ru.practicum.compilations.models.CompilationModel;
import ru.practicum.compilations.repository.CompilationRepository;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.models.EventModel;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.services.EventService;

import java.util.*;

@Service
@AllArgsConstructor
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventService eventService;

    public List<CompilationDto> getList(Boolean pinned, Integer from, Integer size) {
        List<CompilationModel> list;

        if (pinned != null) {
            PageRequest pagination = PageRequest.of(from / size, size);
            list = compilationRepository.getAllByPinned(pinned, pagination);
        } else {
            PageRequest pagination = PageRequest.of(from / size, size);
            list = compilationRepository.findAll(pagination).getContent();
        }

        return CompilationMapper.mapList(list);
    }

    public CompilationDto getCompilation(Long id) {
        Optional<CompilationModel> model = compilationRepository.findById(id);

        if (model.isEmpty()) {
            throw new NotFoundException("Compilation not found");
        }

        return CompilationMapper.map(model.get());
    }

    @Transactional
    public CompilationDto createCompilation(CreateCompilationDto compilationDto) {
        List<EventDto> dtoList = eventService.getListByIds(compilationDto.getEvents());

        if (compilationDto.getEvents() != null) {
            if (dtoList.size() != compilationDto.getEvents().size()) {
                throw new NotFoundException("Event not found");
            }
        }

        CompilationModel model = compilationRepository.save(CompilationMapper.mapCreate(compilationDto));

        return CompilationMapper.map(model, dtoList);
    }

    @Transactional
    public void deleteCompilation(Long id) {
        compilationRepository.deleteById(id);
    }

    @Transactional
    public CompilationDto updateCompilation(Long id, UpdateCompilationDto compilationDto) {
        Optional<CompilationModel> existingModelOp = compilationRepository.findById(id);

        if (existingModelOp.isEmpty()) {
            throw new NotFoundException("Compilation not found");
        }

        CompilationModel existingModel = existingModelOp.get();

        List<EventDto> dtoList = eventService.getListByIds(compilationDto.getEvents());

        if (compilationDto.getEvents() != null) {
            if (dtoList.size() != compilationDto.getEvents().size()) {
                throw new NotFoundException("Event not found");
            }

            Set<EventModel> events = new HashSet<>(EventMapper.mapModelList(dtoList));
            existingModel.setEvents(events);
        }

        if (compilationDto.isPinned() != existingModel.isPinned()) {
            existingModel.setPinned(compilationDto.isPinned());
        }

        if (compilationDto.getTitle() != null) {
            existingModel.setTitle(compilationDto.getTitle());
        }

        CompilationModel model = compilationRepository.save(existingModel);

        return CompilationMapper.map(model);
    }

}
