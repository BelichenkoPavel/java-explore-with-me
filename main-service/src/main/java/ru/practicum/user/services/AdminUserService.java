package ru.practicum.user.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.user.dto.CreateUserDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mappers.UserMapper;
import ru.practicum.user.models.UserModel;
import ru.practicum.user.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminUserService {
    private final UserRepository repository;

    @Transactional
    public UserDto create(CreateUserDto userDto) {
        List<UserModel> list = repository.findAllByEmail(userDto.getEmail());

        if (!list.isEmpty()) {
            throw new ConflictException("User with email " + userDto.getEmail() + " already exists");
        }

        UserModel modelCreate = UserMapper.mapCreate(userDto);

        UserModel model = repository.save(modelCreate);

        return UserMapper.map(model);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<UserDto> getList(List<Long> ids, Integer from, Integer size) {
        PageRequest pagination = PageRequest.of(from / size, size);
        List<UserModel> list;

        if (ids == null) {
            list = repository.findAll(pagination).getContent();
        } else {
            list = repository.findAllByIdIn(ids, pagination);
        }

        return UserMapper.mapList(list);
    }
}
