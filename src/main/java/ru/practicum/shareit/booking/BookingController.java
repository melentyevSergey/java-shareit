package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    private static final String USERID = "X-Sharer-User-Id";

    @PostMapping()
    public BookingDto createBooking(@RequestHeader(USERID) Integer userId,
                                    @RequestBody @Valid BookingDto bookingDto) {
        log.info("Получен POST запрос для добавления нового бронирования.");
        log.debug("Идентификатор пользователя, добавляющий бронь {}", userId);

        // исключения
        // нет пользователя
        // нет вещи
        // дата окончания брони находится в прошлом
        // дата старта брони находится в прошлом
        // дата окончания брони находится раньше даты старта брони
        // дата старта брони равна дате окончания брони
        // дата старта null
        // дата окончания брони null
        return bookingService.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@RequestHeader(USERID) Integer userId,
                                 @PathVariable Integer bookingId,
                                 @RequestParam(name = "approved") Boolean approved) {
        log.info("Получен PATCH запрос для подтверждение или отклонение запроса на бронирование.");
        log.debug("Идентификатор пользователя {}", userId);
        log.debug("Идентификатор брони {}", bookingId);

        return bookingService.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingStatus(@RequestHeader(USERID) Integer userId,
                                       @PathVariable Integer bookingId) {
        log.info("Получен GET запрос для получения данных о конкретном бронировании {}", bookingId);
        log.debug("Идентификатор пользователя в запросе {}", userId);

        // либо автор брони либо владелец вещи
        return bookingService.getBookingStatus(userId, bookingId);
    }

    @GetMapping()
    public List<BookingDto> getBookingListForUserId(@RequestHeader(USERID) Integer userId,
                                                    @RequestParam(defaultValue = "ALL") String state) {
        log.debug("Получен GET запрос на получение списка всех бронирований текущего пользователя.");
        log.debug("Идентификатор пользователя в запросе {}", userId);

        // в ответ отсортированный список по дате от более новых к более старым
        return bookingService.getBookingListForUserId(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getListOfBookedItemsForUserId(@RequestHeader(USERID) Integer userId,
                                                    @RequestParam(defaultValue = "ALL") String state) {
        log.debug("Получен GET запрос на получение списка бронирований для всех вещей текущего пользователя.");
        log.debug("Идентификатор пользователя в запросе {}", userId);

        // проверка на наличие >=1 вещи
        return bookingService.getListOfBookedItemsForUserId(userId, state);
    }
}