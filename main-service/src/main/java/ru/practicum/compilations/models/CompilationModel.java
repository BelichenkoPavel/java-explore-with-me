package ru.practicum.compilations.models;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.events.models.EventModel;

import java.util.Set;

@Entity
@Table(name = "compilations", schema = "public")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompilationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private String title;

    @Column
    private boolean pinned;

    @ManyToMany
    @JoinTable(name = "compilations_events",
            schema = "public",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private Set<EventModel> events;
}
