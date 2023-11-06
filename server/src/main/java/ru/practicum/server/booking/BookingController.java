package ru.practicum.server.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.booking.dto.BookingDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingService bookingService;
    private static final String USERID = "X-Sharer-User-Id";

    @GetMapping("/{bookingId}")
    public BookingDto getById(@RequestHeader(USERID) Integer userId,
                              @PathVariable Integer bookingId) {
        log.info("Потытка получить бронирование с ID: {}", bookingId);
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getByUserId(@RequestHeader(USERID) Integer userId,
                                        @RequestParam(defaultValue = "ALL") String state,
                                        @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                        @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("Потытка получить бронирование по ID пользователя: {}", userId);
        return bookingService.getByUserId(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getByOwnerId(@RequestHeader(USERID) Integer userId,
                                         @RequestParam(defaultValue = "ALL") String state,
                                         @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                         @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("Потытка получить бронирование по ID владельца: {}", userId);
        return bookingService.getByOwnerId(userId, state, from, size);
    }

    @PostMapping
    public BookingDto save(@RequestHeader(USERID) Integer userId,
                           @RequestBody @Valid BookingDto bookingDto) {
        log.info("Попытка добавить бронирование: {}", bookingDto);
        return bookingService.addNewBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@RequestHeader(USERID) Integer userId,
                                     @PathVariable Integer bookingId,
                                     @RequestParam boolean approved) {
        log.info("Попытка подтвердить бронирование с ID {}", bookingId);
        return bookingService.approveBooking(userId, bookingId, approved);
    }
}
