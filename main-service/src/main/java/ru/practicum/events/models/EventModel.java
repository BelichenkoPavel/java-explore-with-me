package ru.practicum.events.models;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.category.models.CategoryModel;
import ru.practicum.user.dto.State;
import ru.practicum.user.models.LocationModel;
import ru.practicum.user.models.UserModel;

import java.time.LocalDateTime;

@Entity
@Table(name = "events", schema = "public")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private String annotation;

    @Column(name = "confirmedRequests")
    private int confirmedRequests;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryModel category;

    @Column
    private LocalDateTime createdOn;

    @Column
    private String description;

    @Column
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "initiator")
    private UserModel initiator;

    @ManyToOne
    @JoinColumn(name = "location")
    private LocationModel location;

    @Column
    private boolean paid;

    @Column
    private int participantLimit;

    @Column
    private LocalDateTime publishedOn;

    @Column
    private boolean requestModeration;

    @Enumerated(value = EnumType.STRING)
    private State state;

    @Column
    private String title;

    @Column
    private int views;
}
