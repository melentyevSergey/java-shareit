package ru.practicum.server.item.model;

import lombok.experimental.UtilityClass;
import ru.practicum.server.item.dto.CommentDto;
import ru.practicum.server.user.User;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CommentMapper {
    public static CommentDto toDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setItemId(comment.getItem().getId());
        commentDto.setAuthorName(comment.getAuthor().getName());
        commentDto.setCreated(comment.getCreated());
        commentDto.setText(comment.getText());
        return commentDto;
    }

    public static Comment toEntity(User user, Item item, CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(commentDto.getCreated());
        comment.setText(commentDto.getText());
        return comment;
    }

    public static List<CommentDto> toDtoList(List<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
    }
}