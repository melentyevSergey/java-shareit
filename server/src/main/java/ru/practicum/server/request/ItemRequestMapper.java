package ru.practicum.server.request;

import lombok.experimental.UtilityClass;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.item.model.ItemMapper;
import ru.practicum.server.request.dto.ItemRequestDto;
import ru.practicum.server.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ItemRequestMapper {

    public static ItemRequest toEntity(User requestor, ItemRequestDto itemRequestDto, List<Item> items) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(itemRequestDto.getId());
        itemRequest.setRequestor(requestor);
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setItems(items);
        return itemRequest;
    }

    public static ItemRequestDto toDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setCreated(itemRequest.getCreated());
        itemRequestDto.setItems(ItemMapper.mapToItemDto(itemRequest.getItems()) != null ? ItemMapper.mapToItemDto(itemRequest.getItems()) : null);
        return itemRequestDto;
    }

    public static List<ItemRequestDto> toDtoList(List<ItemRequest> itemRequests) {
        return itemRequests.stream()
                .map(ItemRequestMapper::toDto)
                .collect(Collectors.toList());
    }
}
