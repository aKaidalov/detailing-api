package ee.detailing.api.businesssettings;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BusinessSettingsService {

    private final BusinessSettingsRepository repository;
    private final BusinessSettingsMapper mapper;

    @Transactional(readOnly = true)
    public BusinessSettingsDto getSettings() {
        BusinessSettings settings = repository.findById(1)
                .orElseThrow(() -> new IllegalStateException("Business settings not configured"));
        return mapper.toDto(settings);
    }

    @Transactional
    public BusinessSettingsDto updateSettings(BusinessSettingsDto dto) {
        BusinessSettings settings = repository.findById(1)
                .orElseThrow(() -> new IllegalStateException("Business settings not configured"));
        mapper.updateEntityFromDto(dto, settings);
        return mapper.toDto(repository.save(settings));
    }
}
