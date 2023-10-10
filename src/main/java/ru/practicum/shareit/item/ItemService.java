package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto getItem(Integer userId, Integer itemId);

    List<ItemDto> getItemsByUserId(Integer userId);

    List<ItemDto> findItem(String text);

    ItemDto createItem(Integer userId, ItemDto itemDto);

    ItemDto updateItem(Integer userId, Integer itemId, ItemDto itemDto);

    void removeItem(Integer userId, Integer itemId);

    Item getItemIfExist(Integer itemId);
}
