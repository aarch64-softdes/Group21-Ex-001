package com.tkpm.sms.application.service.implementation;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.subject.SubjectRequestDto;
import com.tkpm.sms.application.mapper.SubjectMapper;
import com.tkpm.sms.application.service.interfaces.FacultyService;
import com.tkpm.sms.application.service.interfaces.SubjectService;
import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.Subject;
import com.tkpm.sms.domain.repository.SubjectRepository;
import com.tkpm.sms.domain.service.validators.SubjectDomainValidator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubjectServiceImpl implements SubjectService {
    SubjectRepository subjectRepository;
    SubjectDomainValidator subjectValidator;
    SubjectMapper subjectMapper;

    FacultyService facultyService;

    @Override
    public PageResponse<Subject> findAll(BaseCollectionRequest request) {
        PageRequest pageRequest = PageRequest.builder()
                .pageNumber(request.getPage())
                .pageSize(request.getSize())
                .sortBy(request.getSortBy())
                .sortDirection("asc".equalsIgnoreCase(request.getSortDirection())
                        ? PageRequest.SortDirection.ASC
                        : PageRequest.SortDirection.DESC)
                .build();

        return subjectRepository.findAll(pageRequest);
    }

    @Override
    public Subject getSubjectById(Integer id) {
        return subjectRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Subject with id %s not found", id)));
    }

    @Override
    public Subject createSubject(SubjectRequestDto subjectRequestDto) {
        subjectValidator.validateSubjectCodeUniqueness(subjectRequestDto.getCode());
        Subject subject = subjectMapper.toSubject(subjectRequestDto);
        subject.setFaculty(facultyService.getFacultyById(subjectRequestDto.getFacultyId()));

        return subjectRepository.save(subject);
    }

    @Override
    public Subject updateSubject(Integer id, SubjectRequestDto subjectRequestDto) {
        Subject subject = subjectRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Subject with id %s not found", id)));

        if(!subject.getCode().equals(subjectRequestDto.getCode())) {
            subjectValidator.validateSubjectCodeUniqueness(subjectRequestDto.getCode());
        }

        subjectMapper.updateSubjectFromDto(subjectRequestDto, subject);

        return subjectRepository.save(subject);
    }

    @Override
    public void deleteSubject(Integer id) {
        Subject subject = subjectRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Subject with id %s not found", id)));

        subjectRepository.delete(subject);
    }
}
