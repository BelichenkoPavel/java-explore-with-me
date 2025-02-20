package ru.practicum.user.mappers;

import lombok.experimental.UtilityClass;
import ru.practicum.user.dto.CreateUserDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.models.UserModel;

import java.util.List;

@UtilityClass
public class UserMapper {
    public static UserModel mapCreate(CreateUserDto user) {
        return UserModel
                .builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static UserDto map(UserModel model) {
        return UserDto
                .builder()
                .id(model.getId())
                .name(model.getName())
                .email(model.getEmail())
                .build();
    }

    public static UserModel map(UserDto model) {
        return UserModel
                .builder()
                .id(model.getId())
                .name(model.getName())
                .email(model.getEmail())
                .build();
    }

    public static List<UserDto> mapList(List<UserModel> models) {
        return models.stream()
                .map(UserMapper::map)
                .toList();
    }
}
