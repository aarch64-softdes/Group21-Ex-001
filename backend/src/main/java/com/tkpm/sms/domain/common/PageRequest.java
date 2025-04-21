package com.tkpm.sms.domain.common;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
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

    public static PageRequest from(BaseCollectionRequest search) {
        return PageRequest.builder().pageNumber(search.getPage()).pageSize(search.getSize())
                .sortBy(search.getSortBy())
                .sortDirection("desc".equalsIgnoreCase(search.getSortDirection())
                        ? PageRequest.SortDirection.DESC
                        : PageRequest.SortDirection.ASC)
                .build();
    }
}