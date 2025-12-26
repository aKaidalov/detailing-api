package ee.detailing.api.timeslot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeSlotTemplateService {

    private final TimeSlotTemplateRepository repository;
    private final TimeSlotTemplateMapper mapper;

    @Transactional(readOnly = true)
    public List<TimeSlotTemplateDto> getActiveTemplates() {
        return mapper.toDtoList(repository.findAllByIsActiveTrue());
    }

    @Transactional(readOnly = true)
    public List<TimeSlotTemplateDto> getAllTemplates() {
        return mapper.toDtoList(repository.findAll());
    }

    @Transactional(readOnly = true)
    public TimeSlotTemplateDto getTemplate(Integer id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Time slot template not found: " + id));
    }

    @Transactional
    public TimeSlotTemplateDto createTemplate(TimeSlotTemplateDto dto) {
        TimeSlotTemplate entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public TimeSlotTemplateDto updateTemplate(Integer id, TimeSlotTemplateDto dto) {
        TimeSlotTemplate entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Time slot template not found: " + id));
        mapper.updateEntityFromDto(dto, entity);
        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public void deleteTemplate(Integer id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Time slot template not found: " + id);
        }
        repository.deleteById(id);
    }
}
