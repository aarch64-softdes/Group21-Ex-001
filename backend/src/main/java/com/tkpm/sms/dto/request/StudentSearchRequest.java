package com.tkpm.sms.dto.request;

import com.tkpm.sms.dto.request.common.SearchCommonRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentSearchRequest extends SearchCommonRequest {
    private String search; // search by name or by studentId
    private String faculty;
}
