package com.tkpm.sms.presentation.controller;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.program.ProgramRequestDto;
import com.tkpm.sms.application.dto.response.ProgramDto;
import com.tkpm.sms.application.dto.response.common.ApplicationResponseDto;
import com.tkpm.sms.application.mapper.ProgramMapper;
import com.tkpm.sms.application.service.interfaces.ProgramService;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Program;
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
@RequestMapping("/api/programs")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProgramController {
    ProgramService programService;
    ProgramMapper programMapper;

    @GetMapping({"", "/"})
    public ResponseEntity<ApplicationResponseDto<PageResponse<ProgramDto>>> getAllPrograms(
            @ModelAttribute BaseCollectionRequest search) {
        PageResponse<Program> pageResponse = programService.getAllPrograms(search);
        List<ProgramDto> programDtos = ListUtils.transform(pageResponse.getData(),
                programMapper::toDto);
        PageResponse<ProgramDto> listResponse = PageResponse.of(pageResponse, programDtos);
        return ResponseEntity.ok(ApplicationResponseDto.success(listResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<ProgramDto>> getProgram(@PathVariable Integer id) {
        var program = programService.getProgramById(id);
        var programDto = programMapper.toDto(program);

        return ResponseEntity.ok(ApplicationResponseDto.success(programDto));
    }

    @PostMapping({"", "/"})
    public ResponseEntity<ApplicationResponseDto<ProgramDto>> createProgram(
            @Valid @RequestBody ProgramRequestDto program,
            UriComponentsBuilder uriComponentsBuilder) {
        var newProgram = programService.createProgram(program);
        var programDto = programMapper.toDto(newProgram);

        return ResponseEntity
                .created(uriComponentsBuilder.path("/api/programs/{id}")
                        .buildAndExpand(newProgram.getId()).toUri())
                .body(ApplicationResponseDto.success(programDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<ProgramDto>> updateProgram(
            @PathVariable Integer id, @Valid @RequestBody ProgramRequestDto program) {
        var updatedProgram = programService.updateProgram(id, program);
        var programDto = programMapper.toDto(updatedProgram);

        return ResponseEntity.ok(ApplicationResponseDto.success(programDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<Void>> deleteProgram(@PathVariable Integer id) {
        programService.deleteProgram(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}