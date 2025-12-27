package ee.detailing.api.addon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddOnRepository extends JpaRepository<AddOn, Integer> {

    List<AddOn> findAllByIsActiveTrueOrderByDisplayOrderAsc();

    List<AddOn> findAllByOrderByDisplayOrderAsc();

    @Query("SELECT a FROM AddOn a JOIN a.packages p WHERE p.id = :packageId AND a.isActive = true ORDER BY a.displayOrder")
    List<AddOn> findActiveByPackageId(@Param("packageId") Integer packageId);

    @Query("SELECT a FROM AddOn a JOIN a.packages p WHERE a.id IN :ids AND p.id = :packageId")
    List<AddOn> findAllByIdInAndPackagesId(@Param("ids") List<Integer> ids, @Param("packageId") Integer packageId);
}
