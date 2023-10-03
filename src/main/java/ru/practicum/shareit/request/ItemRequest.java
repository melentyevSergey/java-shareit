package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ItemRequest {
    private Integer id;
    private String description;
    private Integer requestor;
    private LocalDate created;
}
