package ru.practicum.server.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.booking.dto.BookingDto;
import ru.practicum.server.exception.NotFoundException;
import ru.practicum.server.exception.ValidationException;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.user.User;
import ru.practicum.server.utility.Utility;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final Utility utility;

    @Transactional
    @Override
    public BookingDto addNewBooking(Integer userId, BookingDto bookingDto) {
        utility.checkCorrectTime(bookingDto.getStart(), bookingDto.getEnd());
        User user = utility.checkUser(userId);
        Item item = utility.checkItem(bookingDto.getItemId());
        utility.checkNotOwner(userId, item);
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь должна быть доступна");
        }
        Booking booking = BookingMapper.toEntity(user, item, bookingDto);
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);
        return BookingMapper.toDto(booking);
    }

    @Transactional
    @Override
    public BookingDto approveBooking(Integer userId, Integer bookingId, boolean approve) {
        utility.checkUser(userId);
        Booking booking = utility.checkBooking(bookingId);
        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new ValidationException(String.format("Бронирование с ID =%d уже подтверждена", bookingId));
        }
        utility.checkOwner(userId, booking.getItem());
        if (approve) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        bookingRepository.save(booking);
        return BookingMapper.toDto(booking);
    }

    @Override
    public List<BookingDto> getByUserId(Integer userId, String state, Integer from, Integer size) {
        utility.checkUser(userId);

        Sort sortByDate = Sort.by(Sort.Direction.ASC, "start");
        int pageIndex = from / size;
        Pageable page = PageRequest.of(pageIndex, size, sortByDate);

        Status status = Status.valueOf(state.toUpperCase());
        switch (status) {
            case ALL:
                return BookingMapper.toDtoList(bookingRepository.getByBookerIdOrderByStartDesc(userId, page).toList());
            case CURRENT:
                return BookingMapper.toDtoList(bookingRepository.getCurrentByUserId(userId, page).toList());
            case PAST:
                return BookingMapper.toDtoList(bookingRepository.findBookingByUserIdAndFinishAfterNow(userId, page).toList());
            case FUTURE:
                return BookingMapper.toDtoList(bookingRepository.findBookingByUserIdAndStartBeforeNow(userId, page).toList());
            case WAITING:
                return BookingMapper.toDtoList(bookingRepository.findBookingByUserIdAndByStatusContainingIgnoreCase(userId, Status.WAITING, page).toList());
            case REJECTED:
                return BookingMapper.toDtoList(bookingRepository.findBookingByUserIdAndByStatusContainingIgnoreCase(userId, Status.REJECTED, page).toList());
        }
        throw new IllegalArgumentException("Unknown state: UNSUPPORTED_STATUS");
    }

    @Override
    public List<BookingDto> getByOwnerId(Integer userId, String state, Integer from, Integer size) {
        utility.checkUser(userId);
        Sort sortByDate = Sort.by(Sort.Direction.ASC, "start");
        int pageIndex = from / size;
        Pageable page = PageRequest.of(pageIndex, size, sortByDate);

        Status status = Status.valueOf(state.toUpperCase());
        switch (status) {
            case ALL:
                return BookingMapper.toDtoList(bookingRepository.findByOwnerId(userId, page).toList());
            case CURRENT:
                return BookingMapper.toDtoList(bookingRepository.getCurrentByOwnerId(userId, page).toList());
            case PAST:
                return BookingMapper.toDtoList(bookingRepository.findPastByOwnerId(userId, page).toList());
            case FUTURE:
                return BookingMapper.toDtoList(bookingRepository.findBookingByOwnerIdAndStartBeforeNow(userId, page).toList());
            case WAITING:
                return BookingMapper.toDtoList(bookingRepository.findBookingByOwnerIdAndByStatusContainingIgnoreCase(userId, Status.WAITING, page).toList());
            case REJECTED:
                return BookingMapper.toDtoList(bookingRepository.findBookingByOwnerIdAndByStatusContainingIgnoreCase(userId, Status.REJECTED, page).toList());
        }
        throw new IllegalArgumentException("Unknown state: UNSUPPORTED_STATUS");
    }

    @Override
    public Optional<Booking> findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(Integer id, LocalDateTime start, Status status) {
        return bookingRepository.findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(id, start, status);
    }

    @Override
    public Optional<Booking> findFirstByItemIdAndEndAfterAndStatusOrderByStartAsc(Integer id, LocalDateTime end, Status status) {
        return bookingRepository.findFirstByItemIdAndEndAfterAndStatusOrderByStartAsc(id, end, status);
    }

    @Override
    public List<Booking> findByItemIn(Iterable<Item> items) {
        return bookingRepository.findByItemIn(items);
    }


    @Override
    public BookingDto getBookingById(Integer userId, Integer bookingId) {
        utility.checkUser(userId);
        Booking booking = utility.checkBooking(bookingId);
        User user = booking.getBooker();
        User owner = booking.getItem().getOwner();
        if (Objects.equals(userId, user.getId()) || Objects.equals(userId, owner.getId())) {
            return BookingMapper.toDto(booking);
        }
        throw new NotFoundException(String.format("Пользователь с ID =%d не является владельцом вещи", userId));
    }



}
