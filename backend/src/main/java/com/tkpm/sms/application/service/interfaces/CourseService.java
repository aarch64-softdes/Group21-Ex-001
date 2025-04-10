package com.tkpm.sms.application.service.interfaces;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.course.CourseCreateRequestDto;
import com.tkpm.sms.application.dto.request.course.CourseUpdateRequestDto;
import com.tkpm.sms.application.dto.request.subject.SubjectRequestDto;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Course;
import com.tkpm.sms.domain.model.Subject;

public interface CourseService {
    PageResponse<Course> findAll(BaseCollectionRequest request);

    Course getCourseById(String id);

    Course createCourse(CourseCreateRequestDto createRequestDto);

    Course updateCourse(String id, CourseUpdateRequestDto updateRequestDto);

    void deleteCourse(String id);

    ///  TODO: Enroll actions need to be defined here
}
