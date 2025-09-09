package ee.joonasvaleting.jvaleting.bookingstatus;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingStatusService {

    private final BookingStatusRepository bookingStatusRepository; //TODO: use universal order
    private final BookingStatusMapper bookingStatusMapper;

    public BookingStatusDto findBookingStatusById(Long id) {
        return bookingStatusMapper.fromDbToDto(
                bookingStatusRepository.findBookingStatusById(id)
                        .orElseThrow(RuntimeException::new)); //TODO: custom exception
    }
}
