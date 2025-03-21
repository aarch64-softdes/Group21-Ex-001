package com.tkpm.sms.dto.request;

import com.tkpm.sms.dto.request.common.BaseCollectionRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCollectionRequest extends BaseCollectionRequest {
    private String search; // search by name or by studentId
    private String faculty;
}
