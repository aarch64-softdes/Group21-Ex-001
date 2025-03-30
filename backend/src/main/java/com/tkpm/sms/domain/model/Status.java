package com.tkpm.sms.domain.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Status {
    Integer id;
    String name;
    LocalDate deletedAt;
    Set<Student> students;
    List<Integer> validTransitionIds;

    public boolean canTransitionTo(Integer targetStatusId) {
        return validTransitionIds != null && validTransitionIds.contains(targetStatusId);
    }

    public boolean canTransitionTo(Status targetStatus) {
        return canTransitionTo(targetStatus.getId());
    }
}