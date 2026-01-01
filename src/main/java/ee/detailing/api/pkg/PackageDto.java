package ee.detailing.api.pkg;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PackageDto {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean isActive;
    private Integer displayOrder;
    private List<Integer> vehicleTypeIds;
}
