package com.tkpm.sms.application.dto.response.status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllowedTransitionDto {
    private Integer toId;
    private String name;
}