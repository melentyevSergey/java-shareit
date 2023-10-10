package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByOwnerId(Integer id);

    void deleteByOwnerIdAndId(Integer userId, Integer itemId);

    List<Item> findItemByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String query, String query1);
}
