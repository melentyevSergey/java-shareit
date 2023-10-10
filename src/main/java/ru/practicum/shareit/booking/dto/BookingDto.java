package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import ru.practicum.shareit.utils.BookingStatus;

import java.time.Instant;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.FutureOrPresent;


@Data
@AllArgsConstructor
public class BookingDto {
    private Integer id;

    @NotNull
    @FutureOrPresent
    private Instant start;

    @NotNull
    @FutureOrPresent
    private Instant finish;

    @NotNull
    private BookingStatus status;
}
