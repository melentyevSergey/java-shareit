package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.utils.ValidationException;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

import static ru.practicum.shareit.utils.AppRequestParams.USERID;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemWithBooking getItem(@PathVariable Integer itemId, @RequestHeader(USERID) Integer userId) {
        log.info("Получен GET запрос для получения вещи по идентификатору {}", itemId);
        log.debug("Идентификатор пользователя, запрашивающий вещь {}", userId);

        return itemService.getItem(userId, itemId);
    }

    @GetMapping()
    public Collection<ItemWithBooking> getItems(@RequestHeader(USERID) Integer userId) {
        log.debug("Получен GET запрос на получение списка всех вещей пользователя.");

        return itemService.getItemsByUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> findItem(@RequestParam(name = "text") String query) {
        log.info("Получен GET запрос для поиск вещи потенциальным арендатором по строке {}", query);

        return itemService.findItem(query);
    }

    @PostMapping()
    public ItemDto createItem(@RequestBody @Valid ItemDto itemDto,
                              @RequestHeader(USERID) Integer userId) throws ValidationException {

        log.info("Получен POST запрос для добавления новой вещи.");
        log.debug("Идентификатор пользователя, добавляющий вещь {}", userId);

        return itemService.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable Integer itemId,
                              @RequestBody ItemDto itemDto,
                              @RequestHeader(USERID) Integer userId) {

        log.info("Получен PATCH запрос для обновления существующей вещи.");
        log.debug("Идентификатор вещи {}", itemId);
        log.debug("Идентификатор пользователя, которому принадлежит вещь {}", userId);

        return itemService.updateItem(userId, itemId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public void removeItem(@RequestHeader(USERID) Integer userId, @PathVariable Integer itemId) {

        log.info("Получен DELETE запрос на удаление вещи с идентификатором {}", itemId);

        itemService.removeItem(userId, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(USERID) Integer userId,
                                 @RequestBody @Valid CommentDto commentDto,
                                 @PathVariable Integer itemId) {

        log.info("Попытка добавить комментарий {}", commentDto);

        return itemService.addNewCommentForItem(userId, commentDto, itemId);
    }
}
