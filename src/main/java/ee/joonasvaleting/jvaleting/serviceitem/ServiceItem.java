package ee.joonasvaleting.jvaleting.serviceitem;

import ee.joonasvaleting.jvaleting.common.entity.AuditingEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class ServiceItem extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_item_name", unique = true, nullable = false)
    @Size(min = 1, max = 150)
    private String serviceItemName;

    @Column
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "is_active")
    private Boolean isActive = Boolean.TRUE;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_service_item_id")
    private ServiceItem parentServiceItem;
}
