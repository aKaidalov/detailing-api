package ee.detailing.api.vehicletype;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VehicleTypeDto {
    private Integer id;
    private String name;
    private BigDecimal basePrice;
    private String description;
    private Boolean isDeliverable;
    private Boolean isActive;
    private Integer displayOrder;
}
