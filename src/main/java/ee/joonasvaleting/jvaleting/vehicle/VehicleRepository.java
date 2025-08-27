package ee.joonasvaleting.jvaleting.vehicle;

import ee.joonasvaleting.jvaleting.vehicletype.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    public List<Vehicle> findAllByVehicleType(VehicleType vehicleType);

    @Query("select vehicle from Vehicle vehicle where lower(vehicle.vehicleType.typeName) = lower(:typeName)")
    public List<Vehicle> findAllByVehicleTypeName(@Param("typeName") String typeName);
}
