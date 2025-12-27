package ee.detailing.api.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Optional<Booking> findByReference(String reference);

    boolean existsByReference(String reference);

    @Query("SELECT b FROM Booking b " +
           "LEFT JOIN FETCH b.vehicleType " +
           "LEFT JOIN FETCH b.pkg " +
           "LEFT JOIN FETCH b.timeSlot ts " +
           "LEFT JOIN FETCH ts.timeSlotTemplate " +
           "LEFT JOIN FETCH b.deliveryType " +
           "LEFT JOIN FETCH b.addOns " +
           "WHERE b.reference = :reference")
    Optional<Booking> findByReferenceWithDetails(@Param("reference") String reference);

    @Query("SELECT b FROM Booking b " +
           "LEFT JOIN FETCH b.vehicleType " +
           "LEFT JOIN FETCH b.pkg " +
           "LEFT JOIN FETCH b.timeSlot ts " +
           "LEFT JOIN FETCH ts.timeSlotTemplate " +
           "LEFT JOIN FETCH b.deliveryType " +
           "LEFT JOIN FETCH b.addOns " +
           "WHERE b.id = :id")
    Optional<Booking> findByIdWithDetails(@Param("id") Integer id);

    @Query("SELECT DISTINCT b FROM Booking b " +
           "LEFT JOIN FETCH b.vehicleType " +
           "LEFT JOIN FETCH b.pkg " +
           "LEFT JOIN FETCH b.timeSlot ts " +
           "LEFT JOIN FETCH ts.timeSlotTemplate " +
           "LEFT JOIN FETCH b.deliveryType " +
           "ORDER BY b.createdAt DESC")
    List<Booking> findAllWithDetails();

    // Analytics queries
    Long countByStatusAndCreatedAtBetween(BookingStatus status, LocalDateTime start, LocalDateTime end);

    Long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COALESCE(SUM(b.totalPrice), 0) FROM Booking b " +
           "WHERE b.status = :status AND b.createdAt BETWEEN :start AND :end")
    BigDecimal sumTotalPriceByStatusAndCreatedAtBetween(
            @Param("status") BookingStatus status,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}
