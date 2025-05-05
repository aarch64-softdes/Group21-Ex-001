package com.tkpm.sms.application.dto.response.status;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
// @JsonInclude(JsonInclude.Include.NON_NULL)
public class StatusDto {
    Integer id;
    String name;

    List<Integer> validTransitionIds;
    List<AllowedTransitionDto> allowedTransitions;
}
