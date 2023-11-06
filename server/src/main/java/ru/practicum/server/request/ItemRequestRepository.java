package ru.practicum.server.request;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {

    List<ItemRequest> findByRequestorId(Integer userId);

    Optional<ItemRequest> findById(Integer userId);
}