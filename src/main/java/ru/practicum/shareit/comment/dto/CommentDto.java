package ru.practicum.shareit.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Integer id;
    private Integer itemId;
    private LocalDateTime created;
    @NotBlank(message = "Комментарий не может быть пустым")
    @Size(min = 1, max = 1000, message = "максимальная длина описания — 1000 символов")
    private String text;
    private String authorName;
}