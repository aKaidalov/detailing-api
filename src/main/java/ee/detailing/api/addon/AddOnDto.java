package ee.detailing.api.addon;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddOnDto {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean isActive;
    private Integer displayOrder;
}
