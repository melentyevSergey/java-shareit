package ru.practicum.server.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ru.practicum.server.booking.dto.BookingDto;
import ru.practicum.server.exception.NotFoundException;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.user.User;
import ru.practicum.server.utility.Utility;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private Utility utility;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void getBookingByIdTest() {
        User user = new User();
        user.setId(1);
        Booking booking = new Booking();
        booking.setId(1);
        booking.setBooker(user);
        Item item = new Item();
        User owner = new User();
        owner.setId(2);
        item.setOwner(owner);
        booking.setItem(item);

        when(utility.checkUser(1)).thenReturn(user);

        when(utility.checkBooking(1)).thenReturn(booking);

        BookingDto actualBooking = bookingService.getBookingById(user.getId(), booking.getId());

        assertEquals(BookingMapper.toDto(booking), actualBooking);
    }

    @Test
    void getBookingByIdTestShouldThrowException() {
        User user = new User();
        user.setId(2);
        Booking booking = new Booking();
        booking.setBooker(user);
        Item item = new Item();
        User owner = new User();
        item.setOwner(owner);
        booking.setItem(item);

        when(utility.checkBooking(1)).thenThrow(new NotFoundException("бронирование не найдено"));

        assertThrows(NotFoundException.class,
                () -> bookingService.getBookingById(1, 1));
    }

    @Test
    void getByUserIdTestAndThrowExceptionIfStateIsWrong() {
        User user = new User();
        user.setId(1);
        user.setName("sergey");
        Item item = new Item();
        item.setId(1);
        Booking booking = new Booking();
        booking.setId(1);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now());
        booking.setBooker(user);
        booking.setItem(item);
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        Integer from = 1;
        Integer size = 10;
        Integer pageIndex = from / size;
        Sort sortByDate = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(pageIndex, size, sortByDate);
        Page<Booking> bookingPage = new PageImpl<>(bookings, page, bookings.size());
        when(utility.checkUser(1)).thenReturn(user);
        when(bookingRepository.getByBookerIdOrderByStartDesc(anyInt(), any(Pageable.class))).thenReturn(bookingPage);
        when(bookingRepository.getCurrentByUserId(anyInt(), any(Pageable.class))).thenReturn(bookingPage);
        when(bookingRepository.findBookingByUserIdAndFinishAfterNow(anyInt(), any(Pageable.class))).thenReturn(bookingPage);
        when(bookingRepository.findBookingByUserIdAndStartBeforeNow(anyInt(), any(Pageable.class))).thenReturn(bookingPage);
        when(bookingRepository.findBookingByUserIdAndByStatusContainingIgnoreCase(anyInt(), any(Status.class), any(Pageable.class))).thenReturn(bookingPage);

        List<BookingDto> actualBookingList = bookingService.getByUserId(1, "ALL", 1, 10);
        assertEquals(BookingMapper.toDtoList(bookings), actualBookingList);
        List<BookingDto> actualBookingListCur = bookingService.getByUserId(1, "CURRENT", 1, 10);
        assertEquals(BookingMapper.toDtoList(bookings), actualBookingListCur);
        List<BookingDto> actualBookingListPast = bookingService.getByUserId(1, "PAST", 1, 10);
        assertEquals(BookingMapper.toDtoList(bookings), actualBookingListPast);
        List<BookingDto> actualBookingListFuture = bookingService.getByUserId(1, "FUTURE", 1, 10);
        assertEquals(BookingMapper.toDtoList(bookings), actualBookingListFuture);
        List<BookingDto> actualBookingListWaiting = bookingService.getByUserId(1, "WAITING", 1, 10);
        assertEquals(BookingMapper.toDtoList(bookings), actualBookingListWaiting);
        List<BookingDto> actualBookingListRejected = bookingService.getByUserId(1, "REJECTED", 1, 10);
        assertEquals(BookingMapper.toDtoList(bookings), actualBookingListRejected);
        assertThrows(IllegalArgumentException.class,
                () -> bookingService.getByUserId(1, "WRONG", 1, 10));
    }

    @Test
    void getByOwnerIdTestAndThrowExceptionIfStateIsWrong() {
        Booking booking = new Booking();
        User user = new User();
        booking.setBooker(user);
        Item item = new Item();
        booking.setItem(item);
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        int from = 1;
        int size = 10;
        int pageIndex = from / size;
        Sort sortByDate = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(pageIndex, size, sortByDate);
        Page<Booking> bookingPage = new PageImpl<>(bookings, page, bookings.size());
        when(utility.checkUser(1)).thenReturn(user);
        when(bookingRepository.getCurrentByOwnerId(anyInt(), any(Pageable.class))).thenReturn(bookingPage);
        when(bookingRepository.findByOwnerId(anyInt(), any(Pageable.class))).thenReturn(bookingPage);
        when(bookingRepository.findPastByOwnerId(anyInt(), any(Pageable.class))).thenReturn(bookingPage);
        when(bookingRepository.findBookingByOwnerIdAndStartBeforeNow(anyInt(), any(Pageable.class))).thenReturn(bookingPage);
        when(bookingRepository.findBookingByOwnerIdAndByStatusContainingIgnoreCase(anyInt(), any(Status.class), any(Pageable.class))).thenReturn(bookingPage);

        List<BookingDto> actualBookingList = bookingService.getByOwnerId(1, "ALL", 1, 10);
        assertEquals(BookingMapper.toDtoList(bookings), actualBookingList);
        List<BookingDto> actualBookingListCur = bookingService.getByOwnerId(1, "CURRENT", 1, 10);
        assertEquals(BookingMapper.toDtoList(bookings), actualBookingListCur);
        List<BookingDto> actualBookingListPast = bookingService.getByOwnerId(1, "PAST", 1, 10);
        assertEquals(BookingMapper.toDtoList(bookings), actualBookingListPast);
        List<BookingDto> actualBookingListFuture = bookingService.getByOwnerId(1, "FUTURE", 1, 10);
        assertEquals(BookingMapper.toDtoList(bookings), actualBookingListFuture);
        List<BookingDto> actualBookingListWaiting = bookingService.getByOwnerId(1, "WAITING", 1, 10);
        assertEquals(BookingMapper.toDtoList(bookings), actualBookingListWaiting);
        List<BookingDto> actualBookingListRejected = bookingService.getByOwnerId(1, "REJECTED", 1, 10);
        assertEquals(BookingMapper.toDtoList(bookings), actualBookingListRejected);
        assertThrows(IllegalArgumentException.class,
                () -> bookingService.getByOwnerId(1, "WRONG", 1, 10));
    }

    @Test
    void approveBookingTestRejected() {
        Booking booking = new Booking();
        booking.setId(1);
        Item item = new Item();
        item.setId(1);
        User user = new User();
        user.setId(1);
        item.setOwner(user);
        booking.setStatus(Status.valueOf("WAITING"));
        booking.setItem(item);
        booking.setBooker(user);
        when(utility.checkUser(1)).thenReturn(user);
        when(utility.checkBooking(anyInt())).thenReturn(booking);

        BookingDto actualBooking = bookingService.approveBooking(1, 1, false);

        assertEquals(BookingMapper.toDto(booking), actualBooking);

        assertEquals(Status.REJECTED, actualBooking.getStatus());

        verify(bookingRepository).save(booking);
    }

    @Test
    void approveBookingTestApproved() {
        Booking booking = new Booking();
        Item item = new Item();
        User user = new User();
        user.setId(1);
        item.setOwner(user);
        booking.setStatus(Status.valueOf("WAITING"));
        booking.setItem(item);
        booking.setBooker(user);

        when(utility.checkUser(1)).thenReturn(user);
        when(utility.checkBooking(anyInt())).thenReturn(booking);

        BookingDto actualBooking = bookingService.approveBooking(1, 1, true);

        assertEquals(BookingMapper.toDto(booking), actualBooking);

        assertEquals(Status.APPROVED, actualBooking.getStatus());

        verify(bookingRepository).save(booking);
    }

    @Test
    void approveBookingTestThrowNotFoundException() {
        User user = new User();
        user.setId(1);
        Item item = new Item();
        item.setOwner(user);
        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setBooker(user);

        when(utility.checkUser(1)).thenReturn(user);
        when(utility.checkBooking(anyInt())).thenThrow(new NotFoundException("бронирование не найдено"));

        assertThrows(NotFoundException.class, () -> bookingService.approveBooking(1, 1, true));
    }

    @Test
    void addNewBookingTest() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.of(2023, 7, 7, 7, 7));
        bookingDto.setEnd(LocalDateTime.of(2023, 7, 8, 7, 7));
        User user = new User();
        user.setId(1);
        User owner = new User();
        owner.setId(2);
        Item item = new Item();
        item.setId(1);
        item.setOwner(owner);
        item.setAvailable(true);
        bookingDto.setItemId(1);
        Booking booking = BookingMapper.toEntity(user, item, bookingDto);
        booking.setStatus(Status.valueOf("WAITING"));
        when(utility.checkUser(1)).thenReturn(user);
        when(utility.checkItem(1)).thenReturn(item);

        BookingDto actualBooking = bookingService.addNewBooking(1, bookingDto);

        assertEquals(BookingMapper.toDto(booking), actualBooking);
        verify(bookingRepository).save(booking);
    }

}
