package com.tkpm.sms.application.dto.request.status;

import com.tkpm.sms.application.annotation.RequiredConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatusRequestDto {
    @RequiredConstraint(field = "Status's name")
    String name;

    List<Integer> validTransitionIds;
}
