package ru.practicum.server.item.model;

import lombok.Data;
import ru.practicum.server.booking.dto.BookingDto;
import ru.practicum.server.item.dto.CommentDto;
import ru.practicum.server.user.User;

import java.util.List;

@Data
public class ItemWithBooking {
    private Integer id;

    private String name;

    private String description;

    private User user;

    private Boolean available;

    private BookingDto lastBooking;

    private BookingDto nextBooking;

    private List<CommentDto> comments;

    private Integer requestId;
}