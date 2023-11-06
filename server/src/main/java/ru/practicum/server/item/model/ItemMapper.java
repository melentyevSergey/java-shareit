package ru.practicum.server.item.model;

import lombok.experimental.UtilityClass;
import ru.practicum.server.booking.dto.BookingDto;
import ru.practicum.server.item.dto.CommentDto;
import ru.practicum.server.item.dto.ItemDto;
import ru.practicum.server.request.ItemRequest;
import ru.practicum.server.user.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@UtilityClass
public class ItemMapper {

    public static ItemDto toDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public static Item toEntity(User owner, ItemDto itemDto, ItemRequest request) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setOwner(owner);
        item.setAvailable(itemDto.getAvailable());
        item.setRequest(request);
        return item;
    }

    public static ItemWithBooking toEntityWithBooking(Item item, BookingDto lastBooking, BookingDto nextBooking, List<CommentDto> comments) {
        ItemWithBooking itemWithBooking = new ItemWithBooking();
        itemWithBooking.setId(item.getId());
        itemWithBooking.setName(item.getName());
        itemWithBooking.setDescription(item.getDescription());
        itemWithBooking.setUser(item.getOwner());
        itemWithBooking.setAvailable(item.getAvailable());
        itemWithBooking.setLastBooking(lastBooking);
        itemWithBooking.setNextBooking(nextBooking);
        itemWithBooking.setComments(comments);
        itemWithBooking.setRequestId(item.getRequest() != null ? item.getRequest().getId() : null);
        return itemWithBooking;
    }

    public static List<ItemDto> mapToItemDto(Iterable<Item> requestItems) {
        if (requestItems == null) {
            return Collections.emptyList();
        }
        return StreamSupport.stream(requestItems.spliterator(), false)
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }
}
