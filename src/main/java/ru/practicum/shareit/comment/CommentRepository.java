package ru.practicum.shareit.comment;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

   List<Comment> findByItemIn(Iterable<Item> items, Sort created);

   List<Comment> findAllByItemId(Integer itemId);
}