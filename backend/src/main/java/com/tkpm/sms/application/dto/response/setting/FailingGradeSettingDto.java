package com.tkpm.sms.application.dto.response.setting;

import com.tkpm.sms.domain.enums.SettingType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FailingGradeSettingDto extends SettingDto {
    Double failingGrade;

    public FailingGradeSettingDto(Double failingGrade) {
        super(SettingType.FAILING_GRADE.getValue());
        this.failingGrade = failingGrade;
    }
}
