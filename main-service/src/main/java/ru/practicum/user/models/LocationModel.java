package ru.practicum.user.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "locations", schema = "public")
@Builder
@Getter
@ToString
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private Float lat;

    @Column
    private Float lon;
}
