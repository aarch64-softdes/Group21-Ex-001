package com.tkpm.sms.domain.common;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PageResponse<T> {
    private final List<T> data;
    private final int pageNumber;
    private final int pageSize;
    private final long totalElements;
    private final int totalPages;

    public static <T> PageResponse<T> of(List<T> data, int pageNumber, int pageSize, long totalElements, int totalPages) {
        return PageResponse.<T>builder()
                .data(data)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .build();
    }

    public static <T, U> PageResponse<T> of(PageResponse<U> page, List<T> list) {
        return PageResponse.<T>builder()
                .data((List<T>) list)
                .pageNumber(page.getPageNumber())
                .pageSize(page.getPageSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}