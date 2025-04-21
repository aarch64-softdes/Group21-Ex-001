package com.tkpm.sms.infrastructure.utils;

import com.tkpm.sms.domain.common.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PagingUtils {
    public static Pageable toSpringPageable(com.tkpm.sms.domain.common.PageRequest request) {
        return org.springframework.data.domain.PageRequest.of(request.getPageNumber() - 1,
                request.getPageSize(),
                request.getSortDirection() == PageRequest.SortDirection.DESC
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC,
                request.getSortBy());
    }
}
