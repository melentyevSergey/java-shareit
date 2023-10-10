package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {

    BookingDto createBooking(Integer userId, BookingDto bookingDto);

    BookingDto approveBooking(Integer userId, Integer bookingId, Boolean approved);

    BookingDto getBookingStatus(Integer userId, Integer bookingId);

    List<BookingDto> getBookingListForUserId(Integer userId, String state);

    List<BookingDto> getListOfBookedItemsForUserId(Integer userId, String state);
}
