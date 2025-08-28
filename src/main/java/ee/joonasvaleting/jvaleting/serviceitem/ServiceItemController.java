package ee.joonasvaleting.jvaleting.serviceitem;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ServiceItemController {

    private final ServiceItemService serviceItemService;

    @GetMapping("/service-items")
    public List<ServiceItemDto> getByParentServiceItemId(@RequestParam Long parentServiceItemId) {
        return serviceItemService.findByParentServiceItemId(parentServiceItemId);
    }
}
