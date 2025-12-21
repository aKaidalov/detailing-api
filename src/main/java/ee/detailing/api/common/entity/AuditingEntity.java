package ee.detailing.api.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;

import java.time.LocalDateTime;

@MappedSuperclass
public class AuditingEntity {

    @Column(name = "created_at")
    private LocalDateTime createdAt;
//    @Column(name = "created_by")
//    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
//    @Column(name = "last_modify_by")
//    private String lastModifyBy;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
}
