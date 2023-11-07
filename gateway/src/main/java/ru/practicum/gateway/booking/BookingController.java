package ru.practicum.gateway.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.gateway.utility.GatewayAppRequestParams.USERID;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> save(@RequestHeader(USERID) Integer userId,
                                       @RequestBody @Valid BookingDto bookingDto) {
        log.info("Попытка добавить бронирование: {}", bookingDto);
        return bookingClient.addNewBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader(USERID) Integer userId,
                                                 @PathVariable Integer bookingId,
                                                 @RequestParam boolean approved) {
        log.info("Потытка получить бронирование с ID: {}", bookingId);
        return bookingClient.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(@RequestHeader(USERID) Integer userId,
                                          @PathVariable Integer bookingId) {
        log.info("Потытка получить бронирование с ID: {}", bookingId);
        return bookingClient.getBookingById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getByUserId(@RequestHeader(USERID) Integer userId,
                                              @RequestParam(name = "state", defaultValue = "ALL") String state,
                                              @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Потытка получить бронирование по ID пользователя: {}", userId);
        return bookingClient.getByUserId(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getByOwnerId(@RequestHeader(USERID) Integer userId,
                                               @RequestParam(name = "state", defaultValue = "ALL") String state,
                                               @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                               @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Потытка получить бронирование по ID владельца: {}", userId);
        return bookingClient.getByOwnerId(userId, state, from, size);
    }
}