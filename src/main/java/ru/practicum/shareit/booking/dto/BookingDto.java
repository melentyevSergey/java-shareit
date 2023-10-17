package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.utils.Status;

import java.time.LocalDateTime;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {

    private Integer id;

    @NotNull
    private Integer itemId;

    private Integer bookerId;

    @Future
    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;

    private Status status;

    private Booker booker;

    private ItemBooking item;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Booker {
        private Integer id;
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemBooking {
        private Integer id;
        private String name;
    }
}
