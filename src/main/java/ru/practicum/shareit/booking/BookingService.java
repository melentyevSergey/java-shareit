package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingService {
    BookingDto getBookingById(Integer userId, Integer bookingId);

    BookingDto createBooking(Integer userId, BookingDto bookingDto);

    BookingDto approveBooking(Integer userId, Integer bookingId, boolean approve);

    List<BookingDto> getBookingListByUserId(Integer userId, String state, Integer from, Integer size);

    List<BookingDto> getBookingListByOwnerId(Integer userId, String state, Integer from, Integer size);

    Optional<Booking> findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(Integer id, LocalDateTime start, Status status);

    Optional<Booking> findFirstByItemIdAndEndAfterAndStatusOrderByStartAsc(Integer id, LocalDateTime end, Status status);

    List<Booking> findByItemIn(Iterable<Item> items);
}
