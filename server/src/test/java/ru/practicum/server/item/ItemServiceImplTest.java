package ru.practicum.server.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.server.booking.Booking;
import ru.practicum.server.booking.BookingRepository;
import ru.practicum.server.booking.Status;
import ru.practicum.server.exception.NotFoundException;
import ru.practicum.server.exception.ValidationException;
import ru.practicum.server.item.dto.CommentDto;
import ru.practicum.server.item.dto.ItemDto;
import ru.practicum.server.item.model.*;
import ru.practicum.server.user.User;
import ru.practicum.server.utility.Utility;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private Utility utility;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    public void getItems_ValidUserId_ReturnsItemWithBookingList() {
        Integer userId = 1;
        Integer from = 0;
        Integer size = 10;

        List<Item> items = Arrays.asList(new Item(), new Item());
        when(utility.checkUser(userId)).thenReturn((new User()));
        when(itemRepository.findByOwnerId(userId, PageRequest.of(0, 10, Sort.Direction.ASC, "id")))
                .thenReturn(new PageImpl<>(items));

        Collection<ItemWithBooking> result = itemService.getItemsByUserId(userId, from, size);

        assertEquals(items.size(), result.size());

    }

    @Test
    public void getItems_InvalidUserId_ThrowsNotFoundException() {
        Integer userId = 1;
        Integer from = 0;
        Integer size = 10;

        when(utility.checkUser(userId)).thenThrow(new NotFoundException("пользователь не найден"));

        assertThrows(NotFoundException.class, () -> itemService.getItemsByUserId(userId, from, size));
    }

    @Test
    public void save_ValidUserIdAndItemDto_ReturnsItemDto() {
        Integer userId = 1;
        ItemDto itemDto = new ItemDto();
        itemDto.setAvailable(true);

        User user = new User();
        when(utility.checkUser(userId)).thenReturn(user);
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ItemDto result = itemService.save(userId, itemDto);

        assertNotNull(result);
        assertEquals(itemDto.getAvailable(), result.getAvailable());
    }

    @Test
    public void save_InvalidUserId_ThrowsNotFoundException() {
        Integer userId = 1;
        ItemDto itemDto = new ItemDto();
        itemDto.setAvailable(true);

        when(utility.checkUser(userId)).thenThrow(new NotFoundException("пользователь не найден"));
        assertThrows(NotFoundException.class, () -> itemService.save(userId, itemDto));
    }

    @Test
    public void save_InvalidUserId_ThrowsValidationException() {
        Integer userId = 1;
        ItemDto itemDto = new ItemDto();

        assertThrows(ValidationException.class, () -> itemService.save(userId, itemDto));
    }

    @Test
    public void getById_ExistingItemId_ReturnsItem() {
        Integer itemId = 1;
        Item item = new Item();
        when(utility.checkItem(itemId)).thenReturn(item);

        ItemDto actualItem = itemService.getById(itemId);

        assertEquals(ItemMapper.toDto(item), actualItem);
    }

    @Test
    public void getById_NonExistingItemId_ThrowsNotFoundException() {
        Integer itemId = 1;
        when(utility.checkItem(itemId)).thenThrow(new NotFoundException("вещь не найдена"));

        assertThrows(NotFoundException.class, () -> itemService.getById(itemId));
    }

    @Test
    public void update_ValidUserIdItemDtoAndItemId_ReturnsUpdatedItemDto() {
        Integer userId = 1;
        Integer itemId = 1;
        ItemDto itemDto = new ItemDto();
        itemDto.setName("New Name");
        itemDto.setDescription("New Description");
        itemDto.setAvailable(true);

        User user = new User();
        Item item = new Item();
        item.setId(itemId);
        when(utility.checkUser(userId)).thenReturn(user);
        when(utility.checkItem(itemId)).thenReturn(item);
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ItemDto result = itemService.update(userId, itemDto, itemId);

        assertNotNull(result);
        assertEquals(itemDto.getName(), result.getName());
        assertEquals(itemDto.getDescription(), result.getDescription());
        assertEquals(itemDto.getAvailable(), result.getAvailable());
    }

    @Test
    public void update_InvalidUserId_ThrowsNotFoundException() {
        Integer userId = 1;
        Integer itemId = 1;
        ItemDto itemDto = new ItemDto();

        when(utility.checkUser(userId)).thenThrow(new NotFoundException("юзер не найден"));

        assertThrows(NotFoundException.class, () -> itemService.update(userId, itemDto, itemId));
    }

    @Test
    public void getByQuery_ValidQuery_ReturnsItemDtoList() {
        String query = "test";
        Integer from = 0;
        Integer size = 10;
        Item item1 = new Item();
        item1.setId(1);
        item1.setName("Item1");
        item1.setDescription("Description 1");
        item1.setAvailable(true);
        Item item2 = new Item();
        item2.setId(2);
        item2.setName("Item2");
        item2.setDescription("Description 2");
        item2.setAvailable(false);
        Item item3 = new Item();
        item3.setId(3);
        item3.setName("Item3");
        item3.setDescription("Description 3");
        item3.setAvailable(true);

        List<Item> itemList = new ArrayList<>();
        itemList.add(item1);
        itemList.add(item2);
        itemList.add(item3);

        when(itemRepository.findItemByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(any(), any(), any(Pageable.class)))
                .thenReturn(itemList);

        List<ItemDto> result = itemService.getByQuery(query, from, size);

        Assertions.assertEquals(2, result.size());
    }

    @Test
    public void getByQuery_EmptyQuery_ReturnsEmptyItemDtoList() {
        String query = "";
        Integer from = 0;
        Integer size = 10;

        List<ItemDto> result = itemService.getByQuery(query, from, size);

        Assertions.assertEquals(0, result.size());
    }

    @Test
    public void removeById_ValidUserAndItem_RemovesItem() {
        Integer userId = 1;
        Integer itemId = 1;
        User user1 = new User();
        user1.setId(userId);
        user1.setEmail("test1@mail.ru");
        user1.setName("Sergey");


        Item item1 = new Item();
        item1.setId(1);
        item1.setName("Item1");
        item1.setDescription("Description 1");
        item1.setAvailable(true);

        when(utility.checkItem(itemId)).thenReturn(item1);
        when(utility.checkUser(userId)).thenReturn(user1);

        itemService.removeById(userId, itemId);

        verify(itemRepository, times(1)).deleteByOwnerIdAndId(userId, itemId);
    }

    @Test
    public void removeById_InvalidUser_ThrowsException() {
        Integer userId = 1;
        Integer itemId = 1;

        when(utility.checkUser(userId)).thenThrow(new NotFoundException("пользователя не существует"));

        assertThrows(NotFoundException.class, () -> {
            itemService.removeById(userId, itemId);
        });

        verify(itemRepository, never()).deleteByOwnerIdAndId(anyInt(), anyInt());
    }

    @Test
    public void addNewComment_UserIsBooker_Success() {
        User user = new User();
        user.setId(1);
        user.setName("User1");

        Item item = new Item();
        item.setId(1);
        item.setName("Item1");

        Booking booking = new Booking();
        booking.setId(1);
        booking.setItem(item);
        booking.setBooker(user);

        Comment comment = new Comment();
        comment.setText("Test comment");
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());

        List<Booking> bookings = Collections.singletonList(booking);

        Sort sort = Sort.by("start").descending();

        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, sort);

        when(bookingRepository.getBookingByBookerIdAndItemIdAndEndBeforeOrderByStartDesc(eq(user.getId()), eq(item.getId()), any(LocalDateTime.class)))
                .thenReturn(bookings);
        when(utility.checkItem(item.getId())).thenReturn(item);
        when(utility.checkUser(user.getId())).thenReturn(user);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDto result = itemService.addNewComment(user.getId(), CommentMapper.toDto(comment), item.getId());

        assertNotNull(result);
        assertEquals(comment.getText(), result.getText());

        verify(bookingRepository).getBookingByBookerIdAndItemIdAndEndBeforeOrderByStartDesc(eq(user.getId()), eq(item.getId()), any(LocalDateTime.class));
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    public void addNewComment_UserIsNotBooker_ThrowsValidationException() {
        User user = new User();
        user.setId(1);
        user.setName("User1");

        Item item = new Item();
        item.setId(1);
        item.setName("Item1");


        List<Booking> bookings = Collections.emptyList();

        when(bookingRepository.getBookingByBookerIdAndItemIdAndEndBeforeOrderByStartDesc(eq(user.getId()), eq(item.getId()), any(LocalDateTime.class)))
                .thenReturn(bookings);

        assertThrows(ValidationException.class, () -> {
            itemService.addNewComment(user.getId(), new CommentDto(), item.getId());
        });

        verify(bookingRepository).getBookingByBookerIdAndItemIdAndEndBeforeOrderByStartDesc(eq(user.getId()), eq(item.getId()), any(LocalDateTime.class));
    }

    @Test
    public void addNewComment_ItemNotFound_ThrowsNotFoundException() {
        User user = new User();
        user.setId(1);
        user.setName("User1");

        Item item = new Item();
        item.setId(1);
        item.setName("Item1");

        Booking booking = new Booking();
        booking.setId(1);
        booking.setItem(item);
        booking.setBooker(user);

        List<Booking> bookings = Collections.singletonList(booking);

        when(bookingRepository.getBookingByBookerIdAndItemIdAndEndBeforeOrderByStartDesc(eq(user.getId()), eq(item.getId()), any(LocalDateTime.class)))
                .thenReturn(bookings);
        when(utility.checkItem(item.getId())).thenThrow(new NotFoundException("вещь не найдена"));

        assertThrows(NotFoundException.class, () -> {
            itemService.addNewComment(user.getId(), new CommentDto(), item.getId());
        });

        verify(bookingRepository).getBookingByBookerIdAndItemIdAndEndBeforeOrderByStartDesc(eq(user.getId()), eq(item.getId()), any(LocalDateTime.class));
    }

    @Test
    public void testGetItemById_WithMatchingUserId() {
        Integer userId = 1;
        Integer itemId = 1;

        User user = new User();
        user.setId(1);
        user.setName("User1");

        Item item = new Item();
        item.setId(itemId);
        item.setOwner(user);
        item.getOwner().setId(userId);

        Booking booking1 = new Booking();
        booking1.setId(1);
        booking1.setStart(LocalDateTime.now().minusDays(2));
        booking1.setStatus(Status.APPROVED);
        booking1.setBooker(user);
        booking1.setItem(item);

        Booking booking2 = new Booking();
        booking2.setId(2);
        booking2.setStart(LocalDateTime.now().plusDays(2));
        booking2.setStatus(Status.APPROVED);
        booking2.setBooker(new User());
        booking2.setItem(item);

        Comment comment = new Comment();
        comment.setId(1);
        comment.setText("Test comment");
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());

        List<Comment> comments = new ArrayList<>();
        comments.add(comment);

        when(bookingRepository.findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(
                eq(itemId), ArgumentMatchers.<LocalDateTime>any(), eq(Status.APPROVED)))
                .thenReturn(Optional.of(booking1));
        when(bookingRepository.findFirstByItemIdAndEndAfterAndStatusOrderByStartAsc(
                eq(itemId), ArgumentMatchers.<LocalDateTime>any(), eq(Status.APPROVED)))
                .thenReturn(Optional.of(booking2));
        when(commentRepository.findAllByItemId(itemId)).thenReturn(comments);
        when(utility.checkItem(itemId)).thenReturn(item);

        ItemWithBooking result = itemService.getItemById(userId, itemId);

        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
        assertEquals(item.getOwner().getId(), result.getUser().getId());
        assertEquals(item.getAvailable(), result.getAvailable());
        assertEquals(booking1.getId(), result.getLastBooking().getId());
        assertEquals(booking2.getId(), result.getNextBooking().getId());
        assertEquals(comment.getId(), result.getComments().get(0).getId());

        verify(bookingRepository).findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(
                eq(itemId), ArgumentMatchers.<LocalDateTime>any(), eq(Status.APPROVED));
        verify(bookingRepository).findFirstByItemIdAndEndAfterAndStatusOrderByStartAsc(
                eq(itemId), ArgumentMatchers.<LocalDateTime>any(), eq(Status.APPROVED));
        verify(commentRepository).findAllByItemId(itemId);
    }
}
