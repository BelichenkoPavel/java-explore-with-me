package ru.practicum.user.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.user.models.UserModel;

import java.util.List;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    List<UserModel> findAllByIdIn(List<Long> ids, PageRequest pageRequest);

    List<UserModel> findAllByEmail(String email);
}
