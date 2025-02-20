package ru.practicum.category.mappers;

import lombok.experimental.UtilityClass;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CreateCategoryDto;
import ru.practicum.category.dto.UpdateCategoryDto;
import ru.practicum.category.models.CategoryModel;

import java.util.List;

@UtilityClass
public class CategoryMapper {
    public static CategoryModel mapCreate(CreateCategoryDto categoryDto) {
        return CategoryModel
                .builder()
                .name(categoryDto.getName())
                .build();
    }

    public static CategoryModel mapUpdate(Long id, UpdateCategoryDto categoryDto) {
        return CategoryModel
                .builder()
                .id(id)
                .name(categoryDto.getName())
                .build();
    }

    public static CategoryDto map(CategoryModel category) {
        return CategoryDto
                .builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static CategoryModel map(CategoryDto category) {
        return CategoryModel
                .builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static List<CategoryDto> mapList(List<CategoryModel> categories) {
        return categories.stream()
                .map(CategoryMapper::map)
                .toList();
    }
}
