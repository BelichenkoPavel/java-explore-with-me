package ru.practicum.requests.models;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.events.models.EventModel;
import ru.practicum.user.models.UserModel;

import java.time.LocalDateTime;

@Entity
@Table(name = "requests", schema = "public")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private EventModel event;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private UserModel requester;

    private LocalDateTime created;

    @Column
    private String status;
}
