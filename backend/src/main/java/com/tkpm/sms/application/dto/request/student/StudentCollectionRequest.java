package com.tkpm.sms.application.dto.request.student;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentCollectionRequest extends BaseCollectionRequest {
    String search; // search by name or by studentId
    
    String faculty;
}
