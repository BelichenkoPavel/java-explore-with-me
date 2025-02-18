package ru.practicum.category.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.mappers.CategoryMapper;
import ru.practicum.category.models.CategoryModel;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryDto> getCategories(Integer from, Integer size) {
        PageRequest pagination = PageRequest.of(from / size, size);

        List<CategoryModel> list = categoryRepository.findAll(pagination).getContent();

        return CategoryMapper.mapList(list);
    }

    public CategoryDto getCategory(Long id) {
        Optional<CategoryModel> model = categoryRepository.findById(id);

        if (model.isEmpty()) {
            throw new NotFoundException("Category not found");
        }

        return CategoryMapper.map(model.get());
    }
}
