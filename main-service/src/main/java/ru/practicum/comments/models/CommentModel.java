package ru.practicum.comments.models;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.comments.dto.CommentState;
import ru.practicum.events.models.EventModel;
import ru.practicum.user.models.UserModel;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments", schema = "public")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private String text;

    @Enumerated(value = EnumType.STRING)
    private CommentState status;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private EventModel event;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    @Column
    private LocalDateTime createDate;
}
