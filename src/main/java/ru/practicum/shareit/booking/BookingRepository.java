package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.utils.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByItemIn(Iterable<Item> items);

    Optional<Booking> findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(
            Integer id, LocalDateTime start, Status status);

    Optional<Booking> findFirstByItemIdAndEndAfterAndStatusOrderByStartAsc(
            Integer id, LocalDateTime end, Status status);

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "JOIN b.booker AS u " +
            "WHERE u.id = ?1 " +
            "ORDER BY b.start DESC")
    List<Booking> findByUserId(Integer userId);

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "JOIN b.booker AS u " +
            "WHERE u.id = ?1 " +
            "AND b.start < current_timestamp AND b.end > current_timestamp " +
            "ORDER BY b.start DESC")
    List<Booking> findCurrentByUserId(Integer userId);

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "JOIN b.booker AS u " +
            "WHERE u.id = ?1 " +
            "and b.end < current_timestamp " +
            "ORDER BY b.start DESC")
    List<Booking> findBookingByUserIdAndFinishAfterNow(Integer userId);

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "JOIN b.booker AS u " +
            "WHERE u.id = ?1 " +
            "and b.start > current_timestamp " +
            "ORDER BY b.start DESC")
    List<Booking> findBookingByUserIdAndStarBeforeNow(Integer userId);

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "JOIN b.booker AS u " +
            "WHERE u.id = ?1 " +
            "and b.status LIKE ?2 " +
            "ORDER BY b.start DESC")
    List<Booking> findBookingByUserIdAndByStatusContainingIgnoreCase(Integer userId, Status state);

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "JOIN b.item AS i " +
            "JOIN i.owner AS o " +
            "WHERE o.id = ?1 " +
            "ORDER BY b.start DESC")
    List<Booking> findByOwnerId(Integer userId);

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "JOIN b.item AS i " +
            "JOIN i.owner AS o " +
            "WHERE o.id = ?1 " +
            "and b.start < current_timestamp and b.end > current_timestamp " +
            "ORDER BY b.start DESC")
    List<Booking> findCurrentByOwnerId(Integer userId);

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "JOIN b.item AS i " +
            "JOIN i.owner AS o " +
            "WHERE o.id = ?1 " +
            "and b.end < current_timestamp " +
            "ORDER BY b.start DESC")
    List<Booking> findPastByOwnerId(Integer userId);

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "JOIN b.item AS i " +
            "JOIN i.owner AS o " +
            "WHERE o.id = ?1 " +
            "and b.start > current_timestamp " +
            "ORDER BY b.start DESC")
    List<Booking> findBookingByOwnerIdAndStarBeforeNow(Integer userId);

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "JOIN b.item AS i " +
            "JOIN i.owner AS o " +
            "WHERE o.id = ?1 " +
            "and b.status LIKE ?2 " +
            "ORDER BY b.start DESC")
    List<Booking> findBookingByOwnerIdAndByStatusContainingIgnoreCase(Integer userId, Status state);
}
