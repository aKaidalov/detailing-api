package ee.joonasvaleting.jvaleting.bookingstatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingStatusDto {
    private Long id;
    private String bookingStatusName;
}
