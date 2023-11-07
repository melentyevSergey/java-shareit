package ru.practicum.server.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.exception.ValidationException;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.item.model.ItemMapper;
import ru.practicum.server.item.model.ItemRepository;
import ru.practicum.server.request.dto.ItemRequestDto;
import ru.practicum.server.utility.Utility;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final Utility utility;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public ItemRequestDto addNewItemRequest(Integer userId, ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = ItemRequestMapper.toEntity(utility.checkUser(userId), itemRequestDto, null);
        return ItemRequestMapper.toDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public Collection<ItemRequestDto> getRequests(Integer userId) {
        utility.checkUser(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.findByRequestorId(userId);
        return mapToRequestWithItems(itemRequests);
    }

    @Override
    public List<ItemRequestDto> getAllRequest(Integer userId, Integer from, Integer size) {
        if (from < 0) {
            throw new ValidationException("FROM не может быть отрицательной");
        }

        Sort sortByDate = Sort.by(Sort.Direction.ASC, "created");
        int pageIndex = from / size;

        Pageable page = PageRequest.of(pageIndex, size, sortByDate);
        Page<ItemRequest> itemRequestPage = itemRequestRepository.findAll(page);
        List<ItemRequest> itemRequestList = itemRequestPage.toList();
        for (ItemRequest itemRequest : itemRequestList) {
            if (Objects.equals(itemRequest.getRequestor().getId(), userId)) {
                if (itemRequestList.size() == 1) {
                    return new ArrayList<>();
                }
                itemRequestList.remove(itemRequest);
            }
        }
        return mapToRequestWithItems(itemRequestPage.toList());
    }

    @Override
    public ItemRequestDto getRequestById(Integer userId, Integer requestId) {
        utility.checkUser(userId);
        ItemRequest itemRequest = utility.checkItemRequest(requestId);
        List<Item> items = itemRepository.findByRequestId(requestId).orElse(null);
        itemRequest.setItems(items);
        return ItemRequestMapper.toDto(itemRequest);
    }

    public List<ItemRequestDto> mapToRequestWithItems(Iterable<ItemRequest> itemRequests) {
        List<ItemRequestDto> itemRequestWithItems = new ArrayList<>();
        List<Integer> requestIds = new ArrayList<>();

        for (ItemRequest request : itemRequests) {
            requestIds.add(request.getId());
            ItemRequestDto requestDto = ItemRequestMapper.toDto(request);
            itemRequestWithItems.add(requestDto);
        }

        List<Item> items = itemRepository.findByRequestIdIn(requestIds);
        Map<Integer, List<Item>> itemsByRequestId = items.stream()
                .collect(Collectors.groupingBy((Item item) -> item.getRequest().getId()));

        for (ItemRequestDto requestDto : itemRequestWithItems) {
            List<Item> requestItems = itemsByRequestId.getOrDefault(requestDto.getId(), Collections.emptyList());
            requestDto.setItems(ItemMapper.mapToItemDto(requestItems));
        }

        return itemRequestWithItems;
    }
}
