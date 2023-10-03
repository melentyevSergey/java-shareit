package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.utils.NotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class InMemoryItemStorage implements ItemStorage {

    private Integer uid = 0;

    /** Поле со списком вещей под уникальным идентификатором пользователя */
    private final Map<Integer, List<Item>> itemsForUserId = new HashMap<>();

    /** Поле с вещью под уникальным идентификатором */
    private final Map<Integer, Item> itemWithId = new HashMap<>();

    @Override
    public Item getItem(Integer itemId) {
        Item item = itemWithId.get(itemId);

        if (item == null) {
            throw new NotFoundException("Не найдена вещь по указанному идентификатору.");
        }
        return item;
    }

    @Override
    public List<Item> getItems(Integer userId) {
        return itemsForUserId.getOrDefault(userId, Collections.emptyList());
    }

    @Override
    public List<ItemDto> findItem(String text) {
        return itemsForUserId.values().stream()
                .flatMap(List::stream)
                .filter(item -> item.getAvailable() &&
                        (item.getName().toUpperCase().contains(text.toUpperCase()) ||
                                item.getDescription().toUpperCase().contains(text.toUpperCase())))
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public Item createItem(Item item) {
        item.setId(++uid);

        itemsForUserId.compute(item.getOwnerId(), (userId, userItems) -> {
            if (userItems == null) {
                userItems = new ArrayList<>();
            }
            userItems.add(item);
            return userItems;
        });

        itemWithId.put(item.getId(), item);

        return item;
    }

    @Override
    public Item updateItem(Item nextItemState) {
        Item currentItem = itemWithId.get(nextItemState.getId());

        if (currentItem == null) {
            throw new NotFoundException("Не найдена вещь по указанному идентификатору.");
        }
        if (nextItemState.getName() == null) {
            nextItemState.setName(currentItem.getName());
        }
        if (nextItemState.getDescription() == null) {
            nextItemState.setDescription(currentItem.getDescription());
        }
        if (nextItemState.getAvailable() == null) {
            nextItemState.setAvailable(currentItem.getAvailable());
        }

        itemsForUserId.compute(nextItemState.getOwnerId(), (userId, userItems) -> {
            int index = userItems.indexOf(currentItem);
            userItems.set(index, nextItemState);
            return userItems;
        });

        itemWithId.put(nextItemState.getId(), nextItemState);
        return nextItemState;
    }

    @Override
    public void deleteItem(Integer userId, Integer itemId) {
        if (itemsForUserId.containsKey(userId)) {
            itemsForUserId.get(userId).removeIf(item -> item.getId().equals(itemId));
        }
        itemWithId.remove(itemId);
    }
}
