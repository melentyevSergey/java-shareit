package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @Test
    void getByIdTest() {
        BookingDto booking = new BookingDto();
        when(bookingService.getBookingById(1, 1)).thenReturn(booking);

        BookingDto response = bookingController.getBookingById(1, 1);

        assertEquals(booking, response);
    }

    @Test
    void getByUserIdTest() {
        List<BookingDto> bookings = List.of(new BookingDto());
        when(bookingService.getBookingListByUserId(1, "ALL", 1, 10)).thenReturn(bookings);

        List<BookingDto> response = bookingController.getBookingListByUserId(1, "ALL", 1, 10);

        assertEquals(bookings, response);
    }

    @Test
    void getByOwnerIdTest() {
        List<BookingDto> bookings = List.of(new BookingDto());
        when(bookingService.getBookingListByOwnerId(1, "ALL", 1, 10)).thenReturn(bookings);

        List<BookingDto> response = bookingController.getBookingListByOwnerId(1, "ALL", 1, 10);

        assertEquals(bookings, response);
    }

    @Test
    void approveBookingTest() {
        BookingDto booking = new BookingDto();
        when(bookingService.approveBooking(anyInt(), anyInt(), anyBoolean())).thenReturn(booking);

        BookingDto response = bookingController.approveBooking(1, 1, false);

        assertEquals(booking, response);
    }

    @Test
    void addTest() {
        Booking booking = new Booking();
        Item item = new Item();
        User user = new User();
        booking.setItem(item);
        booking.setBooker(user);
        when(bookingService.createBooking(1, BookingMapper.toBookingDto(booking))).thenReturn(BookingMapper.toBookingDto(booking));

        BookingDto response = bookingController.createBooking(1, BookingMapper.toBookingDto(booking));

        assertEquals(BookingMapper.toBookingDto(booking), response);
    }
}
