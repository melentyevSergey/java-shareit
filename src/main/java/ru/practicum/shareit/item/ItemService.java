package ru.practicum.shareit.item;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;
import java.util.List;

public interface ItemService {
    ItemWithBooking getItem(Integer userId, Integer itemId);

    Collection<ItemWithBooking> getItemsByUserId(Integer userId);

    List<ItemDto> findItem(String text);

    ItemDto createItem(Integer userId, ItemDto itemDto);

    ItemDto updateItem(Integer userId, Integer itemId, ItemDto itemDto);

    void removeItem(Integer userId, Integer itemId);

    CommentDto addNewCommentForItem(Integer userId, CommentDto commentDto, Integer itemId);
}
