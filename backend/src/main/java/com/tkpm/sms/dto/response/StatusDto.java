package com.tkpm.sms.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tkpm.sms.dto.request.status.AllowedTransitionDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatusDto {
    Integer id;
    String name;

    List<Integer> validTransitionIds;
    List<AllowedTransitionDto> allowedTransitions;
}
