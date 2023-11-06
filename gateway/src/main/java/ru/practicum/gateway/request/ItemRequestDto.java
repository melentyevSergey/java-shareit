package ru.practicum.gateway.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class ItemRequestDto {
    private Integer id;

    @NotBlank
    private String description;
}