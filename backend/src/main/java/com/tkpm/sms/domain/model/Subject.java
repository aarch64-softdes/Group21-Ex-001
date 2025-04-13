package com.tkpm.sms.domain.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Subject {
    Integer id;
    String name;
    String code;
    String description;
    Integer credits;
    Faculty faculty;
    List<Integer> prerequisitesId;
}