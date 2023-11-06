package ru.practicum.server.booking;

import lombok.Data;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private LocalDateTime start;
    @Column(name = "finish")
    private LocalDateTime end;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User booker;
    @Enumerated(EnumType.STRING)
    @Column
    private Status status;
}