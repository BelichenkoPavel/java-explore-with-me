package ru.practicum.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.requests.models.RequestModel;

import java.util.List;

public interface RequestRepository extends JpaRepository<RequestModel, Long> {
    List<RequestModel> findAllByRequesterId(Long id);

    List<RequestModel> findAllByEventId(Long id);

    List<RequestModel> findAllByRequesterIdAndEventId(Long id, Long eventId);

    List<RequestModel> findAllByIdIn(List<Long> ids);
}
