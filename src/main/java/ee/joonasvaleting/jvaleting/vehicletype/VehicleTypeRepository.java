package ee.joonasvaleting.jvaleting.vehicletype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleTypeRepository extends JpaRepository<VehicleType, Long> {

    @Query("select vehicleType from VehicleType vehicleType where vehicleType.id = :id")
    public Optional<VehicleType> findVehicleById(@Param("id") Long id);
}
