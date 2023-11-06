package ru.practicum.server.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private static final String USERID = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestDto save(@RequestHeader(USERID) Integer userId,
                               @RequestBody @Valid ItemRequestDto itemRequestDto) {
        log.info("Попытка добавить запрос: {}", itemRequestDto);
        return itemRequestService.addNewItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public Collection<ItemRequestDto> getRequest(@RequestHeader(USERID) Integer userId) {
        log.info("Попытка получить запросы пользователя: {}", userId);
        return itemRequestService.getRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getRequestAll(@RequestHeader(USERID) Integer userId,
                                              @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("Попытка получить {} запросов", size);
        return itemRequestService.getAllRequest(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestId(@RequestHeader(USERID) Integer userId,
                                       @PathVariable Integer requestId) {
        log.info("Попытка получить запрос по идентификатору {}", requestId);
        return itemRequestService.getRequestById(userId, requestId);
    }

}
