package ee.detailing.api.pkg;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PackageDto {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean isActive;
    private Integer displayOrder;
}
