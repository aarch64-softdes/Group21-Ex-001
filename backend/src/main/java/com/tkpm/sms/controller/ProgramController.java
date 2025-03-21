package com.tkpm.sms.controller;

import com.tkpm.sms.dto.request.ProgramRequestDto;
import com.tkpm.sms.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.dto.response.ProgramDto;
import com.tkpm.sms.dto.response.common.ApplicationResponseDto;
import com.tkpm.sms.dto.response.common.ListResponse;
import com.tkpm.sms.dto.response.common.PageDto;
import com.tkpm.sms.service.ProgramService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import static java.util.stream.Collectors.toList;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/programs")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProgramController {
    ProgramService programService;

    @GetMapping
    public ResponseEntity<ApplicationResponseDto<ListResponse<ProgramDto>>> getAllPrograms(
            @ModelAttribute BaseCollectionRequest search
            ) {
        var programs = programService.getAllPrograms(search);
        var programsDto = programs.stream().map(program -> new ProgramDto(program.getId(), program.getName())).collect(toList());

        var pageDto = PageDto.builder()
                .totalElements(programs.getTotalElements())
                .pageSize(programs.getSize())
                .pageNumber(programs.getNumber())
                .totalPages(programs.getTotalPages())
                .build();

        var listResponse = ListResponse.<ProgramDto>builder().
                page(pageDto).
                data(programsDto).
                build();

        return ResponseEntity.ok(ApplicationResponseDto.success(listResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<ProgramDto>> getProgram(@PathVariable Integer id) {
        var program = programService.getProgramById(id);
        var programDto = new ProgramDto(program.getId(), program.getName());
        return ResponseEntity.ok(ApplicationResponseDto.success(programDto));
    }

    @PostMapping
    public ResponseEntity<ApplicationResponseDto<ProgramDto>> createProgram(
            @Valid @RequestBody ProgramRequestDto program,
            UriComponentsBuilder uriComponentsBuilder) {

        var newProgram = programService.createProgram(program);
        var programDto = new ProgramDto(newProgram.getId(), newProgram.getName());
        return ResponseEntity.created(uriComponentsBuilder.path("/api/programs/{id}").buildAndExpand(newProgram.getId()).toUri())
                .body(ApplicationResponseDto.success(programDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<ProgramDto>> updateProgram(
            @PathVariable Integer id,
            @Valid @RequestBody ProgramRequestDto program) {

        var updatedProgram = programService.updateProgram(id, program);
        var programDto = new ProgramDto(updatedProgram.getId(), updatedProgram.getName());
        return ResponseEntity.ok(ApplicationResponseDto.success(programDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<Void>> deleteProgram(@PathVariable Integer id) {
        programService.deleteProgram(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
