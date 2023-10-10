package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@Table(name = "requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User requestor;

    @Column
    private LocalDateTime created;
}
