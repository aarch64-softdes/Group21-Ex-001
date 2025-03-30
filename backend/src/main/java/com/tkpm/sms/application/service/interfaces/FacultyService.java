package com.tkpm.sms.application.service.interfaces;

import com.tkpm.sms.application.dto.request.faculty.FacultyRequestDto;
import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Faculty;

public interface FacultyService {
    PageResponse<Faculty> getAllFaculties(BaseCollectionRequest search);
    Faculty getFacultyById(Integer id);
    Faculty getFacultyByName(String name);
    Faculty createFaculty(FacultyRequestDto faculty);
    Faculty updateFaculty(Integer id, FacultyRequestDto faculty);
    void deleteFaculty(Integer id);
}