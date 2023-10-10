package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.utils.BookingStatus;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Instant start;

    @Column
    private Instant finish;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @Column
    private BookingStatus status;
}
