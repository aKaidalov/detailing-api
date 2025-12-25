package ee.detailing.api.vehicletype;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleTypeService {

    private final VehicleTypeRepository repository;
    private final VehicleTypeMapper mapper;

    @Transactional(readOnly = true)
    public List<VehicleTypeDto> getActiveVehicleTypes() {
        return mapper.toDtoList(repository.findAllByIsActiveTrueOrderByDisplayOrderAsc());
    }

    @Transactional(readOnly = true)
    public List<VehicleTypeDto> getAllVehicleTypes() {
        return mapper.toDtoList(repository.findAllByOrderByDisplayOrderAsc());
    }

    @Transactional(readOnly = true)
    public VehicleTypeDto getVehicleType(Integer id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle type not found: " + id));
    }

    @Transactional
    public VehicleTypeDto createVehicleType(VehicleTypeDto dto) {
        VehicleType entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public VehicleTypeDto updateVehicleType(Integer id, VehicleTypeDto dto) {
        VehicleType entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle type not found: " + id));
        mapper.updateEntityFromDto(dto, entity);
        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public void deleteVehicleType(Integer id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Vehicle type not found: " + id);
        }
        repository.deleteById(id);
    }
}
