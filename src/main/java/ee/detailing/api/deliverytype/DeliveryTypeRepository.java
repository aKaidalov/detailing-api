package ee.detailing.api.deliverytype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryTypeRepository extends JpaRepository<DeliveryType, Integer> {

    List<DeliveryType> findAllByIsActiveTrue();
}
