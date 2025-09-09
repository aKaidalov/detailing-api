package ee.joonasvaleting.jvaleting.timeslot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    @Query("select timeSlot from TimeSlot timeSlot where timeSlot.id = :id")
    public Optional<TimeSlot> findTimeSlotById(@Param("id") Long id);
}
