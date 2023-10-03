package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    private static final String USERID = "X-Sharer-User-Id";

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable Integer itemId, @RequestHeader(USERID) Integer userId) {
        log.info("Получен GET запрос для получения вещи по идентификатору {}", itemId);
        log.debug("Идентификатор пользователя, запрашивающий вещь {}", userId);

        return itemService.getItem(itemId, userId);
    }

    @GetMapping()
    public List<ItemDto> getItems(@RequestHeader(USERID) Integer userId) {
        log.debug("Получен GET запрос на получение списка всех вещей пользователя.");

        return itemService.getItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> findItem(@RequestParam(name = "text") String query,
                                  @RequestHeader(USERID) Integer userId) {
        log.info("Получен GET запрос для поиск вещи потенциальным арендатором по строке {}", query);

        return itemService.findItem(query, userId);
    }

    @PostMapping()
    public ItemDto createItem(@RequestBody @Valid ItemDto itemDto, @RequestHeader(USERID) Integer userId) {

        log.info("Получен POST запрос для добавления новой вещи.");
        log.debug("Идентификатор пользователя, добавляющий вещь {}", userId);

        return itemService.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable Integer itemId,
                              @RequestBody ItemDto itemDto,
                              @RequestHeader(USERID) Integer userId) {

        log.info("Получен PATCH запрос для обновления существующей вещи.");
        log.debug("Идентификатор вещи {}", itemId);
        log.debug("Идентификатор пользователя, которому принадлежит вещь {}", userId);

        return itemService.updateItem(itemId, itemDto, userId);
    }

    @DeleteMapping("/{itemId}")
    public void removeItem(@RequestHeader(USERID) Integer userId, @PathVariable Integer itemId) {

        log.info("Получен DELETE запрос на удаление вещи с идентификатором {}", itemId);

        itemService.removeItem(userId, itemId);
    }
}
