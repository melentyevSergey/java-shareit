package ru.practicum.server.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.server.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

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
    Page<Booking> findBookingByUserIdAndFinishAfterNow(Integer userId, Pageable page);

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "JOIN b.booker AS u " +
            "WHERE u.id = ?1 " +
            "and b.start > current_timestamp " +
            "ORDER BY b.start DESC")
    Page<Booking> findBookingByUserIdAndStartBeforeNow(Integer userId, Pageable page);

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "JOIN b.booker AS u " +
            "WHERE u.id = ?1 " +
            "and b.status LIKE ?2 " +
            "ORDER BY b.start DESC")
    Page<Booking> findBookingByUserIdAndByStatusContainingIgnoreCase(Integer userId, Status state, Pageable page);

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "JOIN b.item AS i " +
            "JOIN i.owner AS o " +
            "WHERE o.id = ?1 " +
            "ORDER BY b.start DESC")
    Page<Booking> findByOwnerId(Integer userId, Pageable page);

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "JOIN b.item AS i " +
            "JOIN i.owner AS o " +
            "WHERE o.id = ?1 " +
            "and b.start < current_timestamp and b.end > current_timestamp " +
            "ORDER BY b.start DESC")
    Page<Booking> getCurrentByOwnerId(Integer userId, Pageable page);

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "JOIN b.item AS i " +
            "JOIN i.owner AS o " +
            "WHERE o.id = ?1 " +
            "and b.end < current_timestamp " +
            "ORDER BY b.start DESC")
    Page<Booking> findPastByOwnerId(Integer userId, Pageable page);

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "JOIN b.item AS i " +
            "JOIN i.owner AS o " +
            "WHERE o.id = ?1 " +
            "and b.start > current_timestamp " +
            "ORDER BY b.start DESC")
    Page<Booking> findBookingByOwnerIdAndStartBeforeNow(Integer userId, Pageable page);

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "JOIN b.item AS i " +
            "JOIN i.owner AS o " +
            "WHERE o.id = ?1 " +
            "and b.status LIKE ?2 " +
            "ORDER BY b.start DESC")
    Page<Booking> findBookingByOwnerIdAndByStatusContainingIgnoreCase(Integer userId, Status state, Pageable page);

    List<Booking> getBookingByBookerIdAndItemIdAndEndBeforeOrderByStartDesc(Integer userId, Integer itemId, LocalDateTime end);

    Page<Booking> findByBookerIdAndEndIsBefore(Integer bookerId, LocalDateTime currentDateTime, Pageable pageable);

    Optional<Booking> findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(Integer id, LocalDateTime start, Status status);

    Optional<Booking> findFirstByItemIdAndEndAfterAndStatusOrderByStartAsc(Integer id, LocalDateTime end, Status status);

    List<Booking> findByItemIn(Iterable<Item> items);

    Page<Booking> getByBookerIdOrderByStartDesc(Integer userId, Pageable page);

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "JOIN b.booker AS u " +
            "WHERE u.id = ?1 " +
            "and b.start < current_timestamp and b.end > current_timestamp  " +
            "ORDER BY b.start")
    Page<Booking> getCurrentByUserId(Integer userId, Pageable page);
}
