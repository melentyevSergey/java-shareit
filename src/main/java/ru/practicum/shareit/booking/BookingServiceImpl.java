package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.utils.NotFoundException;
import ru.practicum.shareit.utils.Status;
import ru.practicum.shareit.utils.Utility;
import ru.practicum.shareit.utils.ValidationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final Utility utility;

    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public BookingDto createBooking(Integer userId, BookingDto bookingDto) {
        utility.checkCorrectTime(bookingDto.getStart(), bookingDto.getEnd());

        User user = utility.checkUser(userId);
        Item item = utility.checkItem(bookingDto.getItemId());
        utility.checkNotOwner(userId, item);

        if (!item.getAvailable()) {
            throw new ValidationException("Вещь не доступна для бронирования.");
        }

        Booking booking = BookingMapper.toBooking(user, item, bookingDto);
        booking.setStatus(Status.WAITING);

        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDto approveBooking(Integer userId, Integer bookingId, boolean approved) {
        utility.checkUser(userId);
        Booking booking = utility.checkBooking(bookingId);

        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new ValidationException(String.format("Бронирование с ID =%d уже подтверждено.", bookingId));
        }

        utility.checkOwner(userId, booking.getItem());

        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }

        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public List<BookingDto> getBookingListForUserId(Integer userId, String state) {
        utility.checkUser(userId);
        Status status = Status.valueOf(state.toUpperCase());

        switch (status) {
            case ALL:
                return BookingMapper.toBookingDtoList(bookingRepository.findByUserId(userId));
            case CURRENT:
                return BookingMapper.toBookingDtoList(bookingRepository.findCurrentByUserId(userId));
            case PAST:
                return BookingMapper.toBookingDtoList(bookingRepository.findBookingByUserIdAndFinishAfterNow(userId));
            case FUTURE:
                return BookingMapper.toBookingDtoList(bookingRepository.findBookingByUserIdAndStarBeforeNow(userId));
            case WAITING:
                return BookingMapper.toBookingDtoList(bookingRepository.findBookingByUserIdAndByStatusContainingIgnoreCase(userId, Status.WAITING));
            case REJECTED:
                return BookingMapper.toBookingDtoList(bookingRepository.findBookingByUserIdAndByStatusContainingIgnoreCase(userId, Status.REJECTED));
        }
        throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
    }

    @Override
    public List<BookingDto> getListOfBookedItemsForUserId(Integer userId, String state) {
        utility.checkUser(userId);
        Status status = Status.valueOf(state.toUpperCase());

        switch (status) {
            case ALL:
                return BookingMapper.toBookingDtoList(bookingRepository.findByOwnerId(userId));
            case CURRENT:
                return BookingMapper.toBookingDtoList(bookingRepository.findCurrentByOwnerId(userId));
            case PAST:
                return BookingMapper.toBookingDtoList(bookingRepository.findPastByOwnerId(userId));
            case FUTURE:
                return BookingMapper.toBookingDtoList(bookingRepository.findBookingByOwnerIdAndStarBeforeNow(userId));
            case WAITING:
                return BookingMapper.toBookingDtoList(bookingRepository.findBookingByOwnerIdAndByStatusContainingIgnoreCase(userId, Status.WAITING));
            case REJECTED:
                return BookingMapper.toBookingDtoList(bookingRepository.findBookingByOwnerIdAndByStatusContainingIgnoreCase(userId, Status.REJECTED));
        }

        throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
    }

    @Override
    public Optional<Booking> findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(
            Integer id, LocalDateTime start, Status status) {
        return bookingRepository.findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(id, start, status);
    }

    @Override
    public Optional<Booking> findFirstByItemIdAndEndAfterAndStatusOrderByStartAsc(
            Integer id, LocalDateTime end, Status status) {
        return bookingRepository.findFirstByItemIdAndEndAfterAndStatusOrderByStartAsc(id, end, status);
    }

    @Override
    public List<Booking> findBookingByUserIdAndFinishAfterNow(Integer userId) {
        return bookingRepository.findBookingByUserIdAndFinishAfterNow(userId);
    }

    @Override
    public List<Booking> findByItemIn(Iterable<Item> items) {
        return bookingRepository.findByItemIn(items);
    }


    @Override
    public BookingDto getBookingStatus(Integer userId, Integer bookingId) {
        utility.checkUser(userId);
        Booking booking = utility.checkBooking(bookingId);

        User user = booking.getBooker();
        User owner = booking.getItem().getOwner();

        if (Objects.equals(userId, user.getId()) || Objects.equals(userId, owner.getId())) {
            return BookingMapper.toBookingDto(booking);
        }

        throw new NotFoundException(String.format("Пользователь с ID =%d не является владельцом вещи", userId));
    }
}
