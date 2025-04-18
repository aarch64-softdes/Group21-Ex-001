package com.tkpm.sms.application.dto.request.setting;

import com.tkpm.sms.application.annotation.RequiredConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FailingGradeSettingRequestDto {
    @RequiredConstraint(field = "Failing Grade")
    Double failingGrade;
}
