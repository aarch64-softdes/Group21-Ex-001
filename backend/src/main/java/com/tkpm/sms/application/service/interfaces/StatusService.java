package com.tkpm.sms.application.service.interfaces;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.status.StatusRequestDto;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Status;

public interface StatusService {
    PageResponse<Status> getAllStatuses(BaseCollectionRequest search);
    Status getStatusById(Integer id);
    Status getStatusByName(String name);
    Status createStatus(StatusRequestDto statusRequestDto);
    Status updateStatus(Integer id, StatusRequestDto statusRequestDto);
    void deleteStatus(Integer id);
    boolean isTransitionAllowed(Integer fromStatusId, Integer toStatusId);
    boolean isTransitionAllowed(Status fromStatus, Status toStatus);
}