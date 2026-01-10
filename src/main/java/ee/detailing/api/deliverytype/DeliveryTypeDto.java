package ee.detailing.api.deliverytype;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DeliveryTypeDto {
    private Integer id;
    private String name;
    private String icon;
    private BigDecimal price;
    private Boolean requiresAddress;
    private Boolean isActive;
}
