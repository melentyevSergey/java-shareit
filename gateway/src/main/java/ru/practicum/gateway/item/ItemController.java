package ru.practicum.gateway.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    public static final String USERID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> save(@RequestHeader(USERID) Integer userId,
                                       @Valid @RequestBody ItemDto itemDto) {
        log.info("Попытка добавления вещи {}", itemDto);
        return itemClient.save(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(USERID) Integer userId,
                                             @PathVariable Integer itemId,
                                             @RequestBody @Valid CommentDto commentDto) {
        log.info("Попытка добавить комментарий {}", commentDto);
        return itemClient.addComment(userId, itemId, commentDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@NotNull @RequestHeader(USERID) Integer userId,
                                         @PathVariable Integer itemId,
                                         @RequestBody ItemDto itemDto) {
        log.info("Попытка изменить вещь {}", itemDto);
        return itemClient.update(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getByQuery(@RequestParam String text,
                                          @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                          @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Попытка найти вещь по зпросу: {}", text);
        return itemClient.getByQuery(text, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@NotNull @RequestHeader(USERID) Integer userId,
                                          @PathVariable Integer itemId) {
        log.info("Попытка получить вещь с идентификатором {}", itemId);
        return itemClient.getItemById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByUserId(@NotNull @RequestHeader(USERID) Integer userId,
                                      @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                      @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Попытка получить вещи пользователя");
        return itemClient.getItems(userId, from, size);
    }
}