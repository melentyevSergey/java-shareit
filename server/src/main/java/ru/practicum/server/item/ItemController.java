package ru.practicum.server.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.exception.ValidationException;
import ru.practicum.server.item.dto.CommentDto;
import ru.practicum.server.item.dto.ItemDto;
import ru.practicum.server.item.model.ItemService;
import ru.practicum.server.item.model.ItemWithBooking;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemService itemService;

    // TODO REMOVE USERID
    private static final String USERID = "X-Sharer-User-Id";

    @GetMapping
    public Collection<ItemWithBooking> getItemsByUserId(@RequestHeader(USERID) Integer userId,
                                                        @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                        @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("Колличество вещей пользователя {}: {}", userId, itemService.getItemsByUserId(userId, from, size).size());
        return itemService.getItemsByUserId(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ItemWithBooking getById(@RequestHeader(USERID) Integer userId,
                           @PathVariable Integer itemId) {
        log.info("Попытка получить вещь с идентификатором {}", itemId);
        return itemService.getItemById(userId, itemId);
    }

    @PostMapping
    public ItemDto save(@RequestHeader(USERID) Integer userId,
                     @RequestBody @Valid ItemDto itemDto) throws ValidationException {
        log.info("Попытка добавления вещи {}", itemDto);
        return itemService.save(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(USERID) Integer userId,
                          @RequestBody ItemDto itemDto,
                          @PathVariable Integer itemId) {
        log.info("Попытка изменить вещь {}", itemDto);
        return itemService.update(userId, itemDto, itemId);
    }

    @DeleteMapping("/{itemId}")
    public void removeById(@RequestHeader(USERID) Integer userId,
                           @PathVariable Integer itemId) {
        log.info("Попытка удаления вещи с идентификатором {}", userId);
        itemService.removeById(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> getByQuery(@RequestParam(name = "text") String query,
                                    @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                    @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("Попытка найти вещь по зпросу: {}", query);
        return itemService.getByQuery(query, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(USERID) Integer userId,
                                 @RequestBody @Valid CommentDto commentDto,
                                 @PathVariable Integer itemId) {
        log.info("Попытка добавить комментарий {}", commentDto);
        return itemService.addNewComment(userId, commentDto, itemId);
    }
}
