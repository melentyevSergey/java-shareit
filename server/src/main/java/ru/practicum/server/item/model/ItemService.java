package ru.practicum.server.item.model;

import ru.practicum.server.item.dto.CommentDto;
import ru.practicum.server.item.dto.ItemDto;

import java.util.Collection;
import java.util.List;

public interface ItemService {
    ItemDto save(Integer userId, ItemDto itemDto);

//    Collection<ItemDto> getByUserId(Integer userId);

    ItemDto getById(Integer itemId);

    ItemDto update(Integer userId, ItemDto itemDto, Integer itemId);

    List<ItemDto> getByQuery(String query, Integer from, Integer size);

    void removeById(Integer userId, Integer itemId);

    ItemWithBooking getItemById(Integer userId, Integer itemId);

    Collection<ItemWithBooking> getItemsByUserId(Integer userId, Integer from, Integer size);

    CommentDto addNewComment(Integer userId, CommentDto commentDto, Integer itemId);
}