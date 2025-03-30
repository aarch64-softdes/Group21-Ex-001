package com.tkpm.sms.presentation.controller;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.response.FacultyDto;
import com.tkpm.sms.application.dto.response.common.ApplicationResponseDto;
import com.tkpm.sms.application.dto.response.common.ListResponse;
import com.tkpm.sms.application.dto.response.common.PageDto;
import com.tkpm.sms.application.mapper.FacultyMapper;
import com.tkpm.sms.application.service.FacultyService;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Faculty;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/faculties")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FacultyController {
    FacultyService facultyService;
    FacultyMapper facultyMapper;

    @GetMapping({"", "/"})
    public ResponseEntity<ApplicationResponseDto<ListResponse<FacultyDto>>> getAllFaculties(
            @ModelAttribute BaseCollectionRequest search
    ) {
        PageResponse<Faculty> pageResponse = facultyService.getAllFaculties(search);

        // Map domain entities to DTOs
        List<FacultyDto> facultyDtos = pageResponse.getContent().stream()
                .map(facultyMapper::toDto)
                .collect(Collectors.toList());

        // Create page info
        var pageDto = PageDto.builder()
                .totalElements(pageResponse.getTotalElements())
                .pageSize(pageResponse.getPageSize())
                .pageNumber(pageResponse.getPageNumber())
                .totalPages(pageResponse.getTotalPages())
                .build();

        // Create response
        var listResponse = ListResponse.<FacultyDto>builder()
                .page(pageDto)
                .data(facultyDtos)
                .build();

        return ResponseEntity.ok(ApplicationResponseDto.success(listResponse));
    }
}