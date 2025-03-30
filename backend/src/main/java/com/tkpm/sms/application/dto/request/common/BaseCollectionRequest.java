package com.tkpm.sms.application.dto.request.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseCollectionRequest {
    int page = 1;

    int size = 5;
    
    String sortBy = "name";

    String sortDirection = "asc";
}
