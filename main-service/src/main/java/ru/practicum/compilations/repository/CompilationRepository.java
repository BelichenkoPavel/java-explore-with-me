package ru.practicum.compilations.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.compilations.models.CompilationModel;

import java.util.List;

public interface CompilationRepository extends JpaRepository<CompilationModel, Long> {
    List<CompilationModel> getAllByPinned(boolean pinned, PageRequest pageRequest);
}
