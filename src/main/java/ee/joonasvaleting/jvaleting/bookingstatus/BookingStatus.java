package ee.joonasvaleting.jvaleting.bookingstatus;

import ee.joonasvaleting.jvaleting.common.entity.AuditingEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class BookingStatus extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_status_name", nullable = false,  unique = true)
    private String bookingStatusName;
}
