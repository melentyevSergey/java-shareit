package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.utils.NotFoundException;
import ru.practicum.shareit.utils.validators.ValidateItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public ItemDto getItem(Integer itemId, Integer userId) {
        if (!userStorage.isUserPresent(userId)) {
            throw new NotFoundException("Нет пользователя с таким идентификатором.");
        }

        return ItemMapper.toItemDto(itemStorage.getItem(itemId));
    }

    @Override
    public List<ItemDto> getItems(Integer userId) {
        if (!userStorage.isUserPresent(userId)) {
            throw new NotFoundException("Нет пользователя с таким идентификатором.");
        }

        return itemStorage.getItems(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findItem(String text, Integer userId) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }

        if (!userStorage.isUserPresent(userId)) {
            throw new NotFoundException("Нет пользователя с таким идентификатором.");
        }

        return itemStorage.findItem(text);
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, Integer userId) {
        if (!userStorage.isUserPresent(userId)) {
            throw new NotFoundException("Нет пользователя с таким идентификатором.");
        }

        ValidateItem.validate(itemDto);
        Item item = ItemMapper.toItem(userId, itemDto);

        return ItemMapper.toItemDto(itemStorage.createItem(item));
    }

    @Override
    public ItemDto updateItem(Integer itemId, ItemDto itemDto, Integer userId) {
        if (!userStorage.isUserPresent(userId)) {
            throw new NotFoundException("Нет пользователя с таким идентификатором.");
        }

        itemDto.setId(itemId);
        checkItemForUser(userId, itemStorage.getItem(itemDto.getId()));

        return ItemMapper.toItemDto(itemStorage.updateItem(ItemMapper.toItem(userId, itemDto)));
    }

    @Override
    public void removeItem(Integer userId, Integer itemId) {
        if (!userStorage.isUserPresent(userId)) {
            throw new NotFoundException("Нет пользователя с таким идентификатором.");
        }

        checkItemForUser(userId, itemStorage.getItem(itemId));

        itemStorage.deleteItem(userId, itemId);
    }

    private void checkItemForUser(Integer userId, Item item) {
        if (!Objects.equals(userId, item.getOwnerId())) {
            throw new NotFoundException("Вы не являетесь владельцем вещи.");
        }
    }
}
