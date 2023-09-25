package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    public ItemDto getItem(Integer itemId, Integer userId);

    public List<ItemDto> getItems(Integer userId);

    public List<ItemDto> findItem(String text, Integer userId);

    public ItemDto createItem(ItemDto itemDto, Integer userId);

    public ItemDto updateItem(Integer itemId, ItemDto itemDto, Integer userId);

    void removeItem(Integer userId, Integer itemId);
}
