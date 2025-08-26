package ee.joonasvaleting.jvaleting.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

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
}
