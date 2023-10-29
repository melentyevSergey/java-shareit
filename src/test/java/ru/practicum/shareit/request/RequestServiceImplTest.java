package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {
    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private Utility utility;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Test
    void addNewItemRequestTest() {
        ItemRequestDto requestDto = new ItemRequestDto();
        User user = new User();
        user.setId(1);
        when(utility.checkUser(1)).thenReturn(user);

        ItemRequest request = ItemRequestMapper.toEntity(user, requestDto, null);
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(request);

        ItemRequestDto actualRequest = itemRequestService.addNewItemRequest(user.getId(), requestDto);

        assertEquals(ItemRequestMapper.toDto(request), actualRequest);
        verify(itemRequestRepository).save(any(ItemRequest.class));
    }

    @Test
    void getRequestsTest() {
        User user = new User();
        user.setId(1);
        when(utility.checkUser(1)).thenReturn(user);

        ItemRequest itemRequest = new ItemRequest();
        List<ItemRequest> itemRequests = new ArrayList<>();
        itemRequests.add(itemRequest);
        List<ItemRequestDto> itemRequestWithItems = itemRequestService.mapToRequestWithItems(itemRequests);
        when(itemRequestRepository.findByRequestorId(1)).thenReturn(itemRequests);

        List<ItemRequestDto> actualRequests = (List<ItemRequestDto>) itemRequestService.getRequests(1);

        assertEquals(itemRequestWithItems, actualRequests);
    }

    @Test
    void getAllRequestTest() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(new User());
        List<ItemRequest> itemRequests = new ArrayList<>();
        itemRequests.add(itemRequest);
        itemRequest.setItems(new ArrayList<>());
        int from = 1;
        int size = 10;
        int pageIndex = from / size;
        Sort sortByDate = Sort.by(Sort.Direction.ASC, "created");
        Pageable page = PageRequest.of(pageIndex, size, sortByDate);
        Page<ItemRequest> itemRequestPage = new PageImpl<>(itemRequests, page, itemRequests.size());

        when(itemRequestRepository.findAll(any(Pageable.class))).thenReturn(itemRequestPage);

        List<ItemRequestDto> actualItemRequestList = itemRequestService.getAllRequest(1, 1, 10);
        assertEquals(ItemRequestMapper.toDtoList(itemRequests), actualItemRequestList);

    }

    @Test
    void getRequestByIdTest() {
        ItemRequest itemRequest = new ItemRequest();
        Item item = new Item();
        List<Item> items = new ArrayList<>();
        items.add(item);
        when(utility.checkUser(1)).thenReturn(new User());
        when(utility.checkItemRequest(1)).thenReturn(itemRequest);
        itemRequest.setItems(items);

        ItemRequestDto actualItemRequest = itemRequestService.getRequestById(1, 1);

        assertEquals(ItemRequestMapper.toDto(itemRequest), actualItemRequest);
    }
}
