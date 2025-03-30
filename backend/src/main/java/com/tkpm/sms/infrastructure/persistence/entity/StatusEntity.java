package com.tkpm.sms.infrastructure.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "statuses")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE statuses SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class StatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotNull(message = "Status's name is required")
    @Column(unique = true)
    String name;

    @Column(name = "deleted_at")
    LocalDate deletedAt;

//    @OneToMany(mappedBy = "status")
//    Set<StudentEntity> students;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "status_transitions",
            joinColumns = @JoinColumn(name = "from_status_id")
    )
    @Column(name = "to_status_id")
    List<Integer> validTransitionIds;
}