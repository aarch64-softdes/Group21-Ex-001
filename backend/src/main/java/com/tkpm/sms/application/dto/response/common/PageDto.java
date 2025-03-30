package com.tkpm.sms.application.dto.response.common;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageDto {
    int pageNumber;
    int pageSize;
    long totalElements;
    int totalPages;
}
