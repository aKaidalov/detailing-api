package ee.joonasvaleting.jvaleting.serviceitem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceItemDto {
    private Long id;
    private String serviceItemName;
    private String description;
    private BigDecimal price;
    private Boolean isActive;
    private ServiceItemDto parentServiceItem;
}
