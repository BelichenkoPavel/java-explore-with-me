package ru.practicum.events.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.events.models.EventModel;
import ru.practicum.user.models.UserModel;

import java.util.List;
import java.util.Optional;

public interface EventsRepository extends JpaRepository<EventModel, Long>, QuerydslPredicateExecutor<EventModel> {
    List<EventModel> findAllByIdIn(List<Long> ids);

    List<EventModel> findAllByInitiator(UserModel initiator, PageRequest pagenation);

    List<EventModel> findAllByCategoryId(Long categoryId);

    Optional<EventModel> findAllByInitiatorAndId(UserModel initiator, Long id);
}
