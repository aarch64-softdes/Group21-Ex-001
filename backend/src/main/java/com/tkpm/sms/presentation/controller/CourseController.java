package com.tkpm.sms.presentation.controller;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.course.CourseCreateRequestDto;
import com.tkpm.sms.application.dto.request.course.CourseUpdateRequestDto;
import com.tkpm.sms.application.dto.response.common.ApplicationResponseDto;
import com.tkpm.sms.application.dto.response.course.CourseDto;
import com.tkpm.sms.application.dto.response.course.CourseMinimalDto;
import com.tkpm.sms.application.mapper.CourseMapper;
import com.tkpm.sms.application.service.interfaces.CourseService;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Course;
import com.tkpm.sms.domain.utils.ListUtils;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseController {

    CourseService courseService;
    CourseMapper courseMapper;

    @GetMapping({"/", ""})
    public ResponseEntity<ApplicationResponseDto<PageResponse<CourseMinimalDto>>> getCourses(
            @ModelAttribute BaseCollectionRequest search
    ) {
        PageResponse<Course> pageResponse = courseService.findAll(search);
        List<CourseMinimalDto> studentDtos = ListUtils.transform(pageResponse.getData(), courseMapper::toMinimalDto);
        PageResponse<CourseMinimalDto> listResponse = PageResponse.of(pageResponse, studentDtos);
        return ResponseEntity.ok(
                ApplicationResponseDto.success(listResponse)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<CourseDto>> getCourseById(
            @PathVariable Integer id
    ) {
        var course = courseService.getCourseById(id);

        return ResponseEntity.ok(
                ApplicationResponseDto.success(
                        courseMapper.toDto(course)
                )
        );
    }

    @PostMapping({"/", ""})
    public ResponseEntity<ApplicationResponseDto<Object>> createCourse(
            @Valid @RequestBody CourseCreateRequestDto courseCreateRequestDto,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        var course = courseService.createCourse(courseCreateRequestDto);

        var uri = uriComponentsBuilder
                .path("/api/courses/{id}")
                .buildAndExpand(course.getId())
                .toUri();

        return ResponseEntity.created(uri)
                .body(ApplicationResponseDto.success(
                        courseMapper.toDto(course)
                ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<Object>> updateCourse(
            @PathVariable Integer id,
            @Valid @RequestBody CourseUpdateRequestDto courseUpdateRequestDto
    ) {
        var course = courseService.updateCourse(id, courseUpdateRequestDto);

        return ResponseEntity.ok(
                ApplicationResponseDto.success(
                        courseMapper.toDto(course)
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<Void>> deleteCourse(
            @PathVariable Integer id
    ) {
        courseService.deleteCourse(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
