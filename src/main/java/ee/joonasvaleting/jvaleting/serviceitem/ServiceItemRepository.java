package ee.joonasvaleting.jvaleting.serviceitem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceItemRepository extends JpaRepository<ServiceItem, Long> {

    @Query("select serviceItem from ServiceItem serviceItem where serviceItem.parentServiceItem.id = :parentServiceId")
    public List<ServiceItem> findByParentServiceItemId(@Param("parentServiceId") Long parentServiceId);
}
