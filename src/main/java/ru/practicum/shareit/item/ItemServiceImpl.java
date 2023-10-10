package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.utils.NotFoundException;
import ru.practicum.shareit.utils.validators.ValidateItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public ItemDto getItem(Integer userId, Integer itemId) {
        // TODO remove comment
//        userService.getUserIfExist(userId);
//        checkItemForUser(userId, getItemIfExist(itemId));
//
//        return ItemMapper.toItemDto(getItemIfExist(itemId));
    }

    @Override
    public List<ItemDto> getItemsByUserId(Integer userId) {
        userService.getUserIfExist(userId);

        return itemRepository.findByOwnerId(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(toList());
    }

    @Override
    public List<ItemDto> findItem(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }

        List<Item> userItems = itemRepository
                .findItemByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(text, text);

        return userItems.stream()
                .filter(Item::getAvailable)
//                .collect(toList())
//                .stream()
                .map(ItemMapper::toItemDto)
                .collect(toList());
    }

    @Override
    @Transactional
    public ItemDto createItem(Integer userId, ItemDto itemDto) {
        User user = userService.getUserIfExist(userId);

        ValidateItem.validate(itemDto);
        Item item = ItemMapper.toItem(user, itemDto);

        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto updateItem(Integer userId, Integer itemId, ItemDto itemDto) {
        userService.getUserIfExist(userId);
        Item item = getItemIfExist(itemId);

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        itemRepository.save(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    @Transactional
    public void removeItem(Integer userId, Integer itemId) {
        userService.getUserIfExist(userId);

        checkItemForUser(userId, getItemIfExist(itemId));

        itemRepository.deleteByOwnerIdAndId(userId, itemId);
    }

    @Override
    public Item getItemIfExist(Integer itemId) {
        return itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException(String.format("Вещь с идентификатором =%d не найдена", itemId)));
    }

    private void checkItemForUser(Integer userId, Item item) {
        if (!Objects.equals(userId, item.getOwner().getId())) {
            throw new NotFoundException("Вы не являетесь владельцем вещи.");
        }
    }
}
