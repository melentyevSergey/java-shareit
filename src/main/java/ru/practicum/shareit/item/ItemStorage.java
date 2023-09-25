package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemStorage {
    public Item getItem(Integer itemId);

    public List<Item> getItems(Integer userId);

    public List<ItemDto> findItem(String text);

    public Item createItem(Item item);

    public Item updateItem(Item item);

    public void deleteItem(Integer userId, Integer itemId);
}
