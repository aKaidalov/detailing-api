package ee.detailing.api.vehicletype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleTypeRepository extends JpaRepository<VehicleType, Integer> {

    List<VehicleType> findAllByIsActiveTrueOrderByDisplayOrderAsc();

    List<VehicleType> findAllByOrderByDisplayOrderAsc();
}
