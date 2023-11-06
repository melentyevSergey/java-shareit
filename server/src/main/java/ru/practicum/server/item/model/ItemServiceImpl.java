package ru.practicum.server.item.model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.booking.Booking;
import ru.practicum.server.booking.BookingMapper;
import ru.practicum.server.booking.BookingRepository;
import ru.practicum.server.booking.Status;
import ru.practicum.server.booking.dto.BookingDto;
import ru.practicum.server.exception.ValidationException;
import ru.practicum.server.item.dto.CommentDto;
import ru.practicum.server.item.dto.ItemDto;
import ru.practicum.server.request.ItemRequest;
import ru.practicum.server.user.User;
import ru.practicum.server.utility.Utility;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final Utility utility;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public ItemDto save(Integer userId, ItemDto itemDto) {
        if (itemDto.getAvailable() == null || !itemDto.getAvailable()) {
            throw new ValidationException("Вещь должна быть доступна");
        }
        User user = utility.checkUser(userId);

        ItemRequest itemRequest = itemDto.getRequestId() != null ? utility.checkItemRequest(itemDto.getRequestId()) : null;

        Item item = ItemMapper.toEntity(user, itemDto, itemRequest);
        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public ItemDto getById(Integer itemId) {
        return ItemMapper.toDto(utility.checkItem(itemId));
    }

    @Override
    @Transactional
    public ItemDto update(Integer userId, ItemDto itemDto, Integer itemId) {
        utility.checkUser(userId);
        Item item = utility.checkItem(itemId);
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        itemRepository.save(item);
        return ItemMapper.toDto(item);
    }

    @Override
    public List<ItemDto> getByQuery(String query, Integer from, Integer size) {
        Sort sortByDate = Sort.by(Sort.Direction.ASC, "id");
        int pageIndex = from / size;
        Pageable page = PageRequest.of(pageIndex, size, sortByDate);

        if (query.isBlank()) {
            return new ArrayList<>();
        }
        List<Item> userItems = itemRepository.findItemByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query, page);

        return userItems.stream()
                .filter(Item::getAvailable)
                .collect(toList())
                .stream()
                .map(ItemMapper::toDto)
                .collect(toList());
    }

    @Override
    @Transactional
    public void removeById(Integer userId, Integer itemId) {
        utility.checkOwner(utility.checkUser(userId).getId(), utility.checkItem(itemId));
        itemRepository.deleteByOwnerIdAndId(userId, itemId);
    }

    @Override
    public ItemWithBooking getItemById(Integer userId, Integer itemId) {
        Item item = utility.checkItem(itemId);

        BookingDto lastBooking = null;
        BookingDto nextBooking = null;
        if (Objects.equals(userId, item.getOwner().getId())) {
            LocalDateTime currentDateTime = LocalDateTime.now();

            Optional<Booking> lastBookingOptional = bookingRepository
                    .findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(item.getId(), currentDateTime, Status.APPROVED);
            Optional<Booking> nextBookingOptional = bookingRepository
                    .findFirstByItemIdAndEndAfterAndStatusOrderByStartAsc(item.getId(), currentDateTime, Status.APPROVED);

            if (lastBookingOptional.isPresent()) {
                lastBooking = BookingMapper.toDto(lastBookingOptional.get());
            }
            if (nextBookingOptional.isPresent()) {
                Booking nextBookingForDto = nextBookingOptional.get();
                if (lastBookingOptional.isEmpty() || !lastBookingOptional.get().equals(nextBookingForDto)) {
                    nextBooking = BookingMapper.toDto(nextBookingForDto);
                }
            }
        }
        List<CommentDto> comments = CommentMapper.toDtoList(commentRepository.findAllByItemId(itemId));
        return ItemMapper.toEntityWithBooking(item, lastBooking, nextBooking, comments);
    }

    @Override
    public Collection<ItemWithBooking> getItemsByUserId(Integer userId, Integer from, Integer size) {
        utility.checkUser(userId);
        Sort sortByDate = Sort.by(Sort.Direction.ASC, "id");
        int pageIndex = from / size;
        Pageable page = PageRequest.of(pageIndex, size, sortByDate);

        return mapToItemWithBooking(itemRepository.findByOwnerId(userId, page).toList());
    }

    @Override
    @Transactional
    public CommentDto addNewComment(Integer userId, CommentDto commentDto, Integer itemId) {

        Sort sort = Sort.by("start").descending();

        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, sort);

        List<Booking> bookings = bookingRepository.getBookingByBookerIdAndItemIdAndEndBeforeOrderByStartDesc(userId, itemId, LocalDateTime.now());
        boolean userIsBooker = bookings.stream()
                .anyMatch(booking -> Objects.equals(booking.getItem().getId(), itemId));

        if (!userIsBooker) {
            throw new ValidationException("Пользователь не брал в аренду вещь");
        }

        User user = utility.checkUser(userId);
        Item item = utility.checkItem(itemId);

        Comment comment = CommentMapper.toEntity(user, item, commentDto);

        comment.setCreated(LocalDateTime.now());

        comment = commentRepository.save(comment);

        return CommentMapper.toDto(comment);
    }

    private List<ItemWithBooking> mapToItemWithBooking(Iterable<Item> items) {
        List<ItemWithBooking> itemWithBookings = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        Map<Item, List<Comment>> comments = commentRepository.findByItemIn(items,  Sort.by(Sort.Direction.DESC, "created"))
                .stream()
                .collect(Collectors.groupingBy(Comment::getItem));

        Map<Item, List<Booking>> bookingsMap = bookingRepository.findByItemIn(items)
                .stream()
                .collect(Collectors.groupingBy(Booking::getItem));

        for (Item item : items) {
            BookingDto lastBooking = getLastBookingDtoForItem(item, now, bookingsMap.getOrDefault(item, Collections.emptyList()));
            BookingDto nextBooking = getNextBookingDtoForItem(item, now, bookingsMap.getOrDefault(item, Collections.emptyList()));
            List<CommentDto> itemComments = CommentMapper.toDtoList(comments.getOrDefault(item, Collections.emptyList()));
            itemWithBookings.add(ItemMapper.toEntityWithBooking(item, lastBooking, nextBooking, itemComments));

        }

        return itemWithBookings;
    }

    private BookingDto getLastBookingDtoForItem(Item item, LocalDateTime now, List<Booking> bookings) {
        return bookings.stream()
                .filter(booking -> booking.getStart().isBefore(now))
                .max(Comparator.comparing(Booking::getStart))
                .map(BookingMapper::toDto)
                .orElse(null);
    }

    private BookingDto getNextBookingDtoForItem(Item item, LocalDateTime now, List<Booking> bookings) {
        return bookings.stream()
                .filter(booking -> booking.getStart().isAfter(now))
                .min(Comparator.comparing(Booking::getStart))
                .map(BookingMapper::toDto)
                .orElse(null);
    }




}
