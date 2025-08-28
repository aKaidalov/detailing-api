package ee.joonasvaleting.jvaleting.timeslot;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    @GetMapping("/time-slot/{id}")
    public TimeSlotDto getTimeSlotById(@PathVariable Long id){
        TimeSlotDto tsd = timeSlotService.findTimeSlotById(id);
        System.out.println("DTO: " + tsd);
        return tsd;
    }
}
