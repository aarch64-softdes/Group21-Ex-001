package com.tkpm.sms.presentation.controller;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.faculty.FacultyRequestDto;
import com.tkpm.sms.application.dto.response.FacultyDto;
import com.tkpm.sms.application.dto.response.common.ApplicationResponseDto;
import com.tkpm.sms.application.mapper.FacultyMapper;
import com.tkpm.sms.application.service.interfaces.FacultyService;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Faculty;
import com.tkpm.sms.domain.utils.ListUtils;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/faculties")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FacultyController {
    FacultyService facultyService;
    FacultyMapper facultyMapper;

    @GetMapping({"", "/"})
    public ResponseEntity<ApplicationResponseDto<PageResponse<FacultyDto>>> getAllFaculties(
            @ModelAttribute BaseCollectionRequest search) {
        PageResponse<Faculty> pageResponse = facultyService.getAllFaculties(search);
        List<FacultyDto> facultyDtos = ListUtils.transform(pageResponse.getData(),
                facultyMapper::toDto);
        PageResponse<FacultyDto> listResponse = PageResponse.of(pageResponse, facultyDtos);
        return ResponseEntity.ok(ApplicationResponseDto.success(listResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<FacultyDto>> getFaculty(@PathVariable Integer id) {
        var faculty = facultyService.getFacultyById(id);
        var facultyDto = new FacultyDto(faculty.getId(), faculty.getName());

        return ResponseEntity.ok(ApplicationResponseDto.success(facultyDto));
    }

    @PostMapping({"", "/"})
    public ResponseEntity<ApplicationResponseDto<FacultyDto>> createFaculty(
            @Valid @RequestBody FacultyRequestDto faculty,
            UriComponentsBuilder uriComponentsBuilder) {

        var newFaculty = facultyService.createFaculty(faculty);
        var facultyDto = new FacultyDto(newFaculty.getId(), newFaculty.getName());

        return ResponseEntity
                .created(uriComponentsBuilder.path("/api/faculties/{id}")
                        .buildAndExpand(newFaculty.getId()).toUri())
                .body(ApplicationResponseDto.success(facultyDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<FacultyDto>> updateFaculty(
            @PathVariable Integer id, @Valid @RequestBody FacultyRequestDto faculty) {
        var updatedFaculty = facultyService.updateFaculty(id, faculty);
        var facultyDto = new FacultyDto(updatedFaculty.getId(), updatedFaculty.getName());

        return ResponseEntity.ok(ApplicationResponseDto.success(facultyDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<Void>> deleteFaculty(@PathVariable Integer id) {
        facultyService.deleteFaculty(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}