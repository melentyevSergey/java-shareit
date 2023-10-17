package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.utils.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingService {

    BookingDto createBooking(Integer userId, BookingDto bookingDto);

    BookingDto approveBooking(Integer userId, Integer bookingId, boolean approved);

    BookingDto getBookingStatus(Integer userId, Integer bookingId);

    List<BookingDto> getBookingListForUserId(Integer userId, String state);

    List<BookingDto> getListOfBookedItemsForUserId(Integer userId, String state);

    Optional<Booking> findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(Integer id, LocalDateTime start, Status status);

    Optional<Booking> findFirstByItemIdAndEndAfterAndStatusOrderByStartAsc(Integer id, LocalDateTime end, Status status);

    List<Booking> findBookingByUserIdAndFinishAfterNow(Integer userId);

    List<Booking> findByItemIn(Iterable<Item> items);
}
