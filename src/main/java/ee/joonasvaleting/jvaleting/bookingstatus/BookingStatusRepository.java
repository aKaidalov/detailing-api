package ee.joonasvaleting.jvaleting.bookingstatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingStatusRepository extends JpaRepository<BookingStatus, Long> {

    @Query("select bookingStatus from BookingStatus bookingStatus where bookingStatus.id = :bookingStatusId")
    public Optional<BookingStatus> findBookingStatusById(@Param("bookingStatusId") Long bookingStatusId); //TODO: use universal naming
}
