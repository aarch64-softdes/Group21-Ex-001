package com.tkpm.sms.application.dto.request.status;

import com.tkpm.sms.application.annotation.RequiredConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatusVerificationDto {
    @RequiredConstraint(field = "fromId")
    Integer fromId;

    @RequiredConstraint(field = "toId")
    Integer toId;
}
