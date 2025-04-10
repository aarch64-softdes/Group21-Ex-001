package com.tkpm.sms.presentation.controller;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.course.CourseCreateRequestDto;
import com.tkpm.sms.application.dto.request.course.CourseUpdateRequestDto;
import com.tkpm.sms.application.dto.response.common.ApplicationResponseDto;
import com.tkpm.sms.application.dto.response.common.ListResponse;
import com.tkpm.sms.application.dto.response.common.PageDto;
import com.tkpm.sms.application.dto.response.course.CourseDto;
import com.tkpm.sms.application.dto.response.course.CourseMinimalDto;
import com.tkpm.sms.application.mapper.CourseMapper;
import com.tkpm.sms.application.service.interfaces.CourseService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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
    public ResponseEntity<ApplicationResponseDto<ListResponse<CourseMinimalDto>>> getCourses(
            @ModelAttribute BaseCollectionRequest request
    ) {
        if (request.getSortBy().equals("name")) {
            request.setSortBy("id");
        }

        var pageResponse = courseService.findAll(request);

        var pageDto = PageDto.builder()
                .pageNumber(pageResponse.getPageNumber())
                .pageSize(pageResponse.getPageSize())
                .totalElements(pageResponse.getTotalElements())
                .totalPages(pageResponse.getTotalPages())
                .build();

        var courseMinimalDtos = pageResponse.getContent()
                .stream()
                .map(courseMapper::toMinimalDto)
                .toList();

        var listResponse = ListResponse.<CourseMinimalDto>builder()
                .data(courseMinimalDtos)
                .page(pageDto)
                .build();

        return ResponseEntity.ok(
                ApplicationResponseDto.success(listResponse)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<CourseDto>> getCourseById(
            @PathVariable String id
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
            @PathVariable String id,
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
            @PathVariable String id
    ) {
        courseService.deleteCourse(String.valueOf(id));

        return ResponseEntity.noContent().build();
    }
}
