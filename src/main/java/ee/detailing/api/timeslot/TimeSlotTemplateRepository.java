package ee.detailing.api.timeslot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeSlotTemplateRepository extends JpaRepository<TimeSlotTemplate, Integer> {

    List<TimeSlotTemplate> findAllByIsActiveTrue();
}
