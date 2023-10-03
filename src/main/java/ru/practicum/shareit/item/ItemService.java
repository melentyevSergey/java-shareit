package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto getItem(Integer itemId, Integer userId);

    List<ItemDto> getItems(Integer userId);

    List<ItemDto> findItem(String text, Integer userId);

    ItemDto createItem(ItemDto itemDto, Integer userId);

    ItemDto updateItem(Integer itemId, ItemDto itemDto, Integer userId);

    void removeItem(Integer userId, Integer itemId);
}
