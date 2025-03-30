package com.tkpm.sms.domain.common;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class PageResponse<T> {
    private final List<T> content;
    private final int pageNumber;
    private final int pageSize;
    private final long totalElements;
    private final int totalPages;

    public static <T> PageResponse<T> of(List<T> content, int pageNumber, int pageSize, long totalElements, int totalPages) {
        return PageResponse.<T>builder()
                .content(content)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .build();
    }
}