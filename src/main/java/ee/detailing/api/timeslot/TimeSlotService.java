package ee.detailing.api.timeslot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeSlotService {

    private final TimeSlotRepository repository;
    private final TimeSlotTemplateRepository templateRepository;
    private final TimeSlotMapper mapper;

    @Transactional(readOnly = true)
    public List<TimeSlotDto> getAvailableSlots(LocalDate date) {
        return mapper.toDtoList(repository.findAvailableByDate(date));
    }

    @Transactional(readOnly = true)
    public List<TimeSlotDto> getSlotsByDateRange(LocalDate fromDate, LocalDate toDate) {
        return mapper.toDtoList(repository.findByDateRange(fromDate, toDate));
    }

    @Transactional(readOnly = true)
    public TimeSlotDto getSlot(Integer id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Time slot not found: " + id));
    }

    @Transactional
    public TimeSlotDto createSlot(TimeSlotDto dto) {
        TimeSlot entity = mapper.toEntity(dto);

        TimeSlotTemplate template = templateRepository.findById(dto.getTimeSlotTemplateId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Time slot template not found: " + dto.getTimeSlotTemplateId()));
        entity.setTimeSlotTemplate(template);

        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public TimeSlotDto updateSlot(Integer id, TimeSlotDto dto) {
        TimeSlot entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Time slot not found: " + id));

        mapper.updateEntityFromDto(dto, entity);

        if (dto.getTimeSlotTemplateId() != null &&
                !dto.getTimeSlotTemplateId().equals(entity.getTimeSlotTemplate().getId())) {
            TimeSlotTemplate template = templateRepository.findById(dto.getTimeSlotTemplateId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Time slot template not found: " + dto.getTimeSlotTemplateId()));
            entity.setTimeSlotTemplate(template);
        }

        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public void deleteSlot(Integer id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Time slot not found: " + id);
        }
        repository.deleteById(id);
    }
}
