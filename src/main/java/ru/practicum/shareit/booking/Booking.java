package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.AllArgsConstructor;
import ru.practicum.shareit.utils.BookingStatus;

import java.time.Instant;

@Data
@AllArgsConstructor
public class Booking {
    private Integer id;
    private Instant start;
    private Instant end;
    private Integer itemId;
    private Integer bookerId;
    private BookingStatus status;
}
