package ru.practicum.server.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Integer id;

    private Integer itemId;

    private LocalDateTime created;

    @NotBlank(message = "Комментарий не может быть пустым")
    private String text;
    private String authorName;
}