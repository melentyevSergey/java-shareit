package ru.practicum.shareit.booking;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
}
