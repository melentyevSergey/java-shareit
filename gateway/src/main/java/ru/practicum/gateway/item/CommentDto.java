package ru.practicum.gateway.item;

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

    private String authorName;

    private LocalDateTime created;

    @NotBlank
    private String text;
}