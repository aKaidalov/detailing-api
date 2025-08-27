package ee.joonasvaleting.jvaleting.vehicle.type;

import ee.joonasvaleting.jvaleting.common.entity.AuditingEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vehicle_type")
@NoArgsConstructor
@Getter
@Setter
public class VehicleType extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_name", nullable = false, unique = true)
    private String typeName;

}
