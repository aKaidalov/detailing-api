package ee.detailing.api.addon;

import ee.detailing.api.pkg.Package;
import ee.detailing.api.pkg.PackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddOnService {

    private final AddOnRepository repository;
    private final AddOnMapper mapper;
    private final PackageRepository packageRepository;

    @Transactional(readOnly = true)
    public List<AddOnDto> getAddOnsForPackage(Integer packageId) {
        return mapper.toDtoList(repository.findActiveByPackageId(packageId));
    }

    @Transactional(readOnly = true)
    public List<AddOnDto> getAllAddOns() {
        return mapper.toDtoList(repository.findAllByOrderByDisplayOrderAsc());
    }

    @Transactional(readOnly = true)
    public AddOnDto getAddOn(Integer id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Add-on not found: " + id));
    }

    @Transactional
    public AddOnDto createAddOn(AddOnDto dto) {
        AddOn entity = mapper.toEntity(dto);
        updatePackages(entity, dto.getPackageIds());
        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public AddOnDto updateAddOn(Integer id, AddOnDto dto) {
        AddOn entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Add-on not found: " + id));
        mapper.updateEntityFromDto(dto, entity);
        updatePackages(entity, dto.getPackageIds());
        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public void deleteAddOn(Integer id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Add-on not found: " + id);
        }
        repository.deleteById(id);
    }

    private void updatePackages(AddOn entity, List<Integer> packageIds) {
        if (packageIds != null) {
            List<Package> packages = packageRepository.findAllById(packageIds);
            entity.setPackages(new HashSet<>(packages));
        }
    }
}
