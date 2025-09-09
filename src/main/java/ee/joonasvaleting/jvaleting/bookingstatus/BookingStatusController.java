package ee.joonasvaleting.jvaleting.bookingstatus;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookingStatusController {

    private final BookingStatusService bookingStatusService;

    @GetMapping("/booking-status/{id}")
    public BookingStatusDto getBookingStatus(@PathVariable Long id){
        return bookingStatusService.findBookingStatusById(id);
    }
}
