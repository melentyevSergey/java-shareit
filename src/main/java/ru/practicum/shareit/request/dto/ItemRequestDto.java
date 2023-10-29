package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ItemRequestDto {
    private Integer id;
    @NotBlank(message = "описание не может быть пустым")
    @Size(min = 1, max = 88, message = "максимальная длина описания — 88 символов")
    private String description;
    private LocalDateTime created;
    private List<ItemDto> items;
}
