package ru.practicum.shareit.comment;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

   List<Comment> findByItemIn(Iterable<Item> items, Sort created);

   List<Comment> findAllByItemId(Integer itemId);
}