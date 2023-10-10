package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final UserService userService;
    private final ItemService itemService;
    private final BookingRepository bookingRepository;

    @Override
    public BookingDto createBooking(Integer userId, BookingDto bookingDto) {
        //
    }

    @Override
    public BookingDto approveBooking(Integer userId, Integer bookingId, Boolean approved) {
        //
    }

    @Override
    public BookingDto getBookingStatus(Integer userId, Integer bookingId) {
        //
    }

    @Override
    public List<BookingDto> getBookingListForUserId(Integer userId, String state) {
        //
    }

    @Override
    public List<BookingDto> getListOfBookedItemsForUserId(Integer userId, String state) {
        //
    }
}
