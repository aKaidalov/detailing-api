package ee.detailing.api.pkg;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<Package, Integer> {

    List<Package> findAllByIsActiveTrueOrderByDisplayOrderAsc();

    List<Package> findAllByOrderByDisplayOrderAsc();

    @Query("SELECT p FROM Package p JOIN p.vehicleTypes vt WHERE vt.id = :vehicleTypeId AND p.isActive = true ORDER BY p.displayOrder")
    List<Package> findActiveByVehicleTypeId(@Param("vehicleTypeId") Integer vehicleTypeId);

    boolean existsByIdAndVehicleTypesId(Integer id, Integer vehicleTypesId);
}
