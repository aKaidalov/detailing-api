package ee.detailing.api.timeslot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Integer> {

    @Query("SELECT ts FROM TimeSlot ts JOIN FETCH ts.timeSlotTemplate tst " +
           "WHERE ts.date = :date AND ts.status = 'AVAILABLE' AND tst.isActive = true " +
           "ORDER BY tst.startTime")
    List<TimeSlot> findAvailableByDate(@Param("date") LocalDate date);

    @Query("SELECT ts FROM TimeSlot ts JOIN FETCH ts.timeSlotTemplate " +
           "WHERE ts.date >= :fromDate AND ts.date <= :toDate " +
           "ORDER BY ts.date, ts.timeSlotTemplate.startTime")
    List<TimeSlot> findByDateRange(@Param("fromDate") LocalDate fromDate,
                                   @Param("toDate") LocalDate toDate);
}
