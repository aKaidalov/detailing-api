package ee.detailing.api.businesssettings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessSettingsRepository extends JpaRepository<BusinessSettings, Integer> {
}
