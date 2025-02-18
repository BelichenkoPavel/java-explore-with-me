package ru.practicum.user.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.user.dto.*;
import ru.practicum.user.mappers.UserMapper;
import ru.practicum.user.models.UserModel;
import ru.practicum.user.repository.UserRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto getById(Long id) {
        Optional<UserModel> model = userRepository.findById(id);

        if (model.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        return UserMapper.map(model.get());
    }
}
