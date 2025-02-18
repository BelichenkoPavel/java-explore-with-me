package ru.practicum.category.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CreateCategoryDto;
import ru.practicum.category.dto.UpdateCategoryDto;
import ru.practicum.category.mappers.CategoryMapper;
import ru.practicum.category.models.CategoryModel;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.services.EventService;
import ru.practicum.exceptions.ConflictException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminCategoryService {
    private final CategoryRepository categoryRepository;

    private final CategoryService categoryService;

    private final EventService eventService;

    public void deleteCategory(Long id) {
        CategoryDto dto = categoryService.getCategory(id);

        List<EventDto> events = eventService.findByCategory(dto.getId());

        if (!events.isEmpty()) {
            throw new ConflictException("Category is used in events");
        }

        categoryRepository.deleteById(id);
    }


    public CategoryDto create(CreateCategoryDto categoryDto) {
        Optional<CategoryModel> categoryModel = categoryRepository.findByName(categoryDto.getName());

        if (categoryModel.isPresent()) {
            throw new ConflictException("Category with this name already exists");
        }

        CategoryModel model = categoryRepository.save(CategoryMapper.mapCreate(categoryDto));

        return CategoryMapper.map(model);
    }

    public CategoryDto update(Long id, UpdateCategoryDto categoryDto) {
        Optional<CategoryModel> categoryModel = categoryRepository.findByName(categoryDto.getName());

        if (categoryModel.isPresent() && !categoryModel.get().getId().equals(id)) {
            throw new ConflictException("Category with this name already exists");
        }

        categoryService.getCategory(id);

        CategoryModel model = categoryRepository.save(CategoryMapper.mapUpdate(id, categoryDto));

        return CategoryMapper.map(model);
    }
}
