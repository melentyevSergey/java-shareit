package ru.practicum.server.utility;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.server.booking.Booking;
import ru.practicum.server.booking.BookingRepository;
import ru.practicum.server.exception.NotFoundException;
import ru.practicum.server.exception.ValidationException;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.item.model.ItemRepository;
import ru.practicum.server.request.ItemRequest;
import ru.practicum.server.request.ItemRequestRepository;
import ru.practicum.server.user.User;
import ru.practicum.server.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class Utility {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository itemRequestRepository;


    public ItemRequest checkItemRequest(Integer requestId) {
        return itemRequestRepository.findById(requestId).orElseThrow(()
                -> new NotFoundException(String.format("Запрос с идентификатором =%d не найден", requestId)));
    }

    public void checkOwner(Integer userId, Item item) {
        if (!Objects.equals(userId, item.getOwner().getId())) {
            throw new NotFoundException("Вы не являетесь владельцем");
        }
    }

    public void checkNotOwner(Integer userId, Item item) {
        if (Objects.equals(userId, item.getOwner().getId())) {
            throw new NotFoundException("Вы являетесь владельцем");
        }
    }

    public User checkUser(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь с идентификатором =%d не найден", userId)));
    }

    public Booking checkBooking(Integer bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException(String.format("Бронирование с идентификатором =%d не найден", bookingId)));
    }

    public Item checkItem(Integer itemId) {
        return itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException(String.format("Вещь с идентификатором =%d не найдена", itemId)));
    }

    public void checkCorrectTime(LocalDateTime start, LocalDateTime end) {
        if (!start.isBefore(end)) {
            throw new ValidationException("Дата окончания бронирования должна быть после его начала");
        }
    }
}
