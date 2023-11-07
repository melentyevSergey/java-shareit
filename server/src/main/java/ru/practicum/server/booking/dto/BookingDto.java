package ru.practicum.server.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.server.booking.Status;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private Integer id;

    @NotNull
    private Integer itemId;

    private Integer bookerId;

    @NotNull
    @Future
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;

    private Status status;

    private Booker booker;

    private ItemBooking item;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Booker {
        private Integer id;
        private String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemBooking {
        private Integer id;
        private String name;
    }
}
