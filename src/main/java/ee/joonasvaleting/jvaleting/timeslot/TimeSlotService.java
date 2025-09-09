package ee.joonasvaleting.jvaleting.timeslot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;
    private final TimeSlotMapper timeSlotMapper;

    public TimeSlotDto findTimeSlotById(Long id){

        TimeSlotDto tsd = timeSlotMapper.fromDbToDto(
                timeSlotRepository.findTimeSlotById(id)
                        .orElseThrow(RuntimeException::new)); //TODO: custom exception

        System.out.println("Service received: " + tsd.getId());

        return tsd;
    }
}
