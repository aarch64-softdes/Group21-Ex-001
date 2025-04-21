package com.tkpm.sms.application.dto.response.setting;

import com.tkpm.sms.domain.enums.SettingType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdjustmentDurationSettingDto extends SettingDto {
    String adjustmentDuration;

    public AdjustmentDurationSettingDto(String adjustmentDuration) {
        super(SettingType.ADJUSTMENT_DURATION.getValue());
        this.adjustmentDuration = adjustmentDuration;
    }
}
