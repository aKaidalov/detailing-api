package ee.detailing.api.booking;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookingAddOnDto {
    private Integer id;
    private String name;
    private BigDecimal price;
}
