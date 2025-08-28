package ee.joonasvaleting.jvaleting.serviceitem;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceItemService {

    private final ServiceItemRepository serviceItemRepository;
    private final ServiceItemMapper serviceItemMapper;

    public List<ServiceItemDto> findByParentServiceItemId(Long parentServiceItemId) {
        return serviceItemRepository
                .findByParentServiceItemId(parentServiceItemId)
                .stream()
                .map(serviceItemMapper::fromDbToDto)
                .toList();
    }
}
