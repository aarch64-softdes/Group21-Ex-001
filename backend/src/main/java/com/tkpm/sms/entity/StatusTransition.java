package com.tkpm.sms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Entity
@Table(name = "status_transitions", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"from_status_id", "to_status_id"}))
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE status_transitions SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class StatusTransition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "from_status_id", nullable = false)
    @NotNull(message = "From status is required")
    Status fromStatus;

    @ManyToOne
    @JoinColumn(name = "to_status_id", nullable = false)
    @NotNull(message = "To status is required")
    Status toStatus;

    @Column(name = "deleted_at")
    LocalDate deletedAt;
}