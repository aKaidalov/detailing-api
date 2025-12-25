package ee.detailing.api.pkg;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PackageService {

    private final PackageRepository repository;
    private final PackageMapper mapper;

    @Transactional(readOnly = true)
    public List<PackageDto> getPackagesForVehicleType(Integer vehicleTypeId) {
        return mapper.toDtoList(repository.findActiveByVehicleTypeId(vehicleTypeId));
    }

    @Transactional(readOnly = true)
    public List<PackageDto> getAllPackages() {
        return mapper.toDtoList(repository.findAllByOrderByDisplayOrderAsc());
    }

    @Transactional(readOnly = true)
    public PackageDto getPackage(Integer id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Package not found: " + id));
    }

    @Transactional
    public PackageDto createPackage(PackageDto dto) {
        Package entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public PackageDto updatePackage(Integer id, PackageDto dto) {
        Package entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Package not found: " + id));
        mapper.updateEntityFromDto(dto, entity);
        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public void deletePackage(Integer id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Package not found: " + id);
        }
        repository.deleteById(id);
    }
}
