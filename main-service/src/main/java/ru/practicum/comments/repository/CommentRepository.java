package ru.practicum.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.comments.dto.CommentState;
import ru.practicum.comments.models.CommentModel;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentModel, Long>, QuerydslPredicateExecutor<CommentModel> {
    Optional<CommentModel> findByIdAndUserId(Long id, Long userId);

    List<CommentModel> findAllByEventIdAndStatus(Long eventId, CommentState status);
}
