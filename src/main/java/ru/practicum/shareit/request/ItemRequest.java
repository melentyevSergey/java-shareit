package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Entity
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

    @Transient
    private List<Item> items;
}