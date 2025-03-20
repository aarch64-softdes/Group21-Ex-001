package com.tkpm.sms.dto.request.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchCommonRequest {
    private int page = 1;
    private int size = 5;
    private String sortBy = "name";
    private String sortDirection = "asc";
}
