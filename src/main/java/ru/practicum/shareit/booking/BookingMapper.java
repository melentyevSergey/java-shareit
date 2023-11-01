package ru.practicum.shareit.booking;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class BookingMapper {
    public static Booking toBookingEntity(User user, Item item, BookingDto bookingDto) {
        Booking booking = new Booking();
        booking.setId(bookingDto.getId());
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        return booking;
    }

    public static BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());

        BookingDto.Booker user = new BookingDto.Booker();
        user.setId(booking.getBooker().getId());
        user.setName(booking.getBooker().getName());
        bookingDto.setBooker(user);

        BookingDto.ItemBooking item = new BookingDto.ItemBooking();
        item.setId(booking.getItem().getId());
        item.setName(booking.getItem().getName());
        bookingDto.setItem(item);
        bookingDto.setItemId(booking.getItem().getId());
        bookingDto.setBookerId(booking.getBooker().getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setStatus(booking.getStatus());

        return bookingDto;
    }

    public static List<BookingDto> toBookingDtoList(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}
