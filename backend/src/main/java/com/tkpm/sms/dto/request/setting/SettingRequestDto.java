package com.tkpm.sms.dto.request.setting;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SettingRequestDto {
    @NotEmpty(message = "NOT_NULL; Setting's name is required")
    String name;
    @NotEmpty(message = "NOT_NULL; Setting's details is required")
    List<String> details;
}
