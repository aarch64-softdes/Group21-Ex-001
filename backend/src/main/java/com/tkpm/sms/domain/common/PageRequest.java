package com.tkpm.sms.domain.common;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageRequest {
    private final int pageNumber;
    private final int pageSize;
    private final String sortBy;
    private final SortDirection sortDirection;

    public enum SortDirection {
        ASC, DESC
    }
}