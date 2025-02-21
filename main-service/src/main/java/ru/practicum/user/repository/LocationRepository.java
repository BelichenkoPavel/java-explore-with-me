package ru.practicum.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.user.models.LocationModel;

public interface LocationRepository extends JpaRepository<LocationModel, Long> {
}
