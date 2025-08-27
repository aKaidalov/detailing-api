package ee.joonasvaleting.jvaleting.vehicle;

import ee.joonasvaleting.jvaleting.common.entity.AuditingEntity;
import ee.joonasvaleting.jvaleting.vehicletype.VehicleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Vehicle extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "licence_plate", unique = true)
    @Size(min = 2, max = 20)
    private String licencePlate;

    @Column(name = "vin_code", unique = true)
    @Size(min = 17, max = 17)
    private String vinCode;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_type_id")
    private VehicleType vehicleType;
}
