package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemStorage {
    Item getItem(Integer itemId);

    List<Item> getItems(Integer userId);

    List<ItemDto> findItem(String text);

    Item createItem(Item item);

    Item updateItem(Item item);

    void deleteItem(Integer userId, Integer itemId);
}
