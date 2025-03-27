package com.tkpm.sms.dto.request.setting;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.tkpm.sms.validator.required.RequiredConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SettingRequestDto {
    @RequiredConstraint(field = "Setting's name")
    String name;
    @RequiredConstraint(field = "Setting's details")
    List<String> details;

    @JsonSetter
    public void setName(String name){
        this.name = (name != null) ? name.toLowerCase() : null;
    }
}
