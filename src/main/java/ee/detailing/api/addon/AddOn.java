package ee.detailing.api.addon;

import ee.detailing.api.common.entity.AuditingEntity;
import ee.detailing.api.pkg.Package;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "add_on")
@Getter
@Setter
@NoArgsConstructor
public class AddOn extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = 0;

    @ManyToMany
    @JoinTable(
            name = "package_add_on",
            joinColumns = @JoinColumn(name = "add_on_id"),
            inverseJoinColumns = @JoinColumn(name = "package_id")
    )
    private Set<Package> packages = new HashSet<>();
}
