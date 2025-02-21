package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.models.Hit;
import ru.practicum.models.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface IHitDBRepository extends JpaRepository<Hit, Long> {
    @Query(value = "SELECT new ru.practicum.models.Stat(app, uri, 1l) FROM Hit h WHERE uri IN (?1) and h.timestamp BETWEEN ?2 AND ?3 GROUP BY app, uri order by count(uri) desc")
    List<Stat> findByUriInUnique(String[] uris, LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.models.Stat(app, uri, count(uri)) FROM Hit h WHERE uri IN (?1) and h.timestamp BETWEEN ?2 AND ?3 GROUP BY app, uri order by count(uri) desc")
    List<Stat> findGroupByUriIn(String[] uris, LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.models.Stat(app, uri, count(uri)) FROM Hit h WHERE h.timestamp BETWEEN ?1 AND ?2 GROUP BY app, uri order by count(uri) desc")
    List<Stat> findGroupByUri(LocalDateTime start, LocalDateTime end);
}
