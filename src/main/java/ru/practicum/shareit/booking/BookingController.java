package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.shareit.utility.AppRequestParams.USERID;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader(USERID) Integer userId,
                                     @PathVariable Integer bookingId) {
        log.info("Потытка получить бронирование с ID: {}", bookingId);

        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getBookingListByUserId(@RequestHeader(USERID) Integer userId,
                                                   @RequestParam(defaultValue = "ALL") String state,
                                                   @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                   @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("Потытка получить бронирование по ID пользователя: {}", userId);

        return bookingService.getBookingListByUserId(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingListByOwnerId(@RequestHeader(USERID) Integer userId,
                                                    @RequestParam(defaultValue = "ALL") String state,
                                                    @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                    @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("Потытка получить бронирование по ID владельца: {}", userId);

        return bookingService.getBookingListByOwnerId(userId, state, from, size);
    }

    @PostMapping
    public BookingDto createBooking(@RequestHeader(USERID) Integer userId,
                                    @RequestBody @Valid BookingDto bookingDto) {
        log.info("Попытка добавить бронирование: {}", bookingDto);

        return bookingService.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@RequestHeader(USERID) Integer userId,
                                     @PathVariable Integer bookingId,
                                     @RequestParam boolean approved) {
        log.info("Попытка подтвердить бронирование с ID {}", bookingId);
        return bookingService.approveBooking(userId, bookingId, approved);
    }
}
