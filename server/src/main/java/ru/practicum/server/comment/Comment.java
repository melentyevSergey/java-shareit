package ru.practicum.server.comment;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @Column
    private LocalDateTime created;

    @Column
    private String text;
}