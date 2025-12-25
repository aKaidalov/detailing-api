package ee.detailing.api.deliverytype;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryTypeService {

    private final DeliveryTypeRepository repository;
    private final DeliveryTypeMapper mapper;

    @Transactional(readOnly = true)
    public List<DeliveryTypeDto> getActiveDeliveryTypes() {
        return mapper.toDtoList(repository.findAllByIsActiveTrue());
    }

    @Transactional(readOnly = true)
    public List<DeliveryTypeDto> getAllDeliveryTypes() {
        return mapper.toDtoList(repository.findAll());
    }

    @Transactional(readOnly = true)
    public DeliveryTypeDto getDeliveryType(Integer id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Delivery type not found: " + id));
    }

    @Transactional
    public DeliveryTypeDto createDeliveryType(DeliveryTypeDto dto) {
        DeliveryType entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public DeliveryTypeDto updateDeliveryType(Integer id, DeliveryTypeDto dto) {
        DeliveryType entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Delivery type not found: " + id));
        mapper.updateEntityFromDto(dto, entity);
        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public void deleteDeliveryType(Integer id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Delivery type not found: " + id);
        }
        repository.deleteById(id);
    }
}
