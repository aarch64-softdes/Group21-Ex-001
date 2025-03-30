package com.tkpm.sms.application.service.implementation;

import com.tkpm.sms.application.annotation.TranslateDomainException;
import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.faculty.FacultyRequestDto;
import com.tkpm.sms.application.exception.ApplicationException;
import com.tkpm.sms.domain.exception.ErrorCode;
import com.tkpm.sms.application.exception.ExceptionTranslator;
import com.tkpm.sms.application.service.interfaces.FacultyService;
import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Faculty;
import com.tkpm.sms.domain.exception.DomainException;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.repository.FacultyRepository;
import com.tkpm.sms.domain.service.validators.FacultyDomainValidator;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FacultyServiceImpl implements FacultyService {
    FacultyRepository facultyRepository;
    FacultyDomainValidator facultyValidator;

    @Override
    public PageResponse<Faculty> getAllFaculties(BaseCollectionRequest search) {
        // Convert to domain PageRequest
        PageRequest pageRequest = PageRequest.builder()
                .pageNumber(search.getPage())
                .pageSize(search.getSize())
                .sortBy(search.getSortBy())
                .sortDirection("desc".equalsIgnoreCase(search.getSortDirection())
                        ? PageRequest.SortDirection.DESC
                        : PageRequest.SortDirection.ASC)
                .build();

        return facultyRepository.findAll(pageRequest);
    }

    @Override
    @TranslateDomainException
    public Faculty getFacultyById(Integer id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Faculty with id %s not found", id)
                ));
    }

    @Override
    @TranslateDomainException
    public Faculty getFacultyByName(String name) {
        return facultyRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Faculty with name %s not found", name)
                ));
    }

    @Override
    @Transactional
    @TranslateDomainException
    public Faculty createFaculty(FacultyRequestDto faculty) {
        // Use domain service for business validation
        facultyValidator.validateNameUniqueness(faculty.getName());

        var newFaculty = Faculty.builder().name(faculty.getName()).build();
        return facultyRepository.save(newFaculty);
    }

    @Override
    @Transactional
    @TranslateDomainException
    public Faculty updateFaculty(Integer id, FacultyRequestDto faculty) {
        // Use domain service for business validation
        facultyValidator.validateNameUniquenessForUpdate(faculty.getName(), id);

        Faculty facultyToUpdate = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Faculty with id %s not found", id)
                ));

        facultyToUpdate.setName(faculty.getName());
        return facultyRepository.save(facultyToUpdate);
    }

    @Override
    @Transactional
    @TranslateDomainException
    public void deleteFaculty(Integer id) {
            Faculty faculty = facultyRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("Faculty with id %s not found", id)
                    ));

            faculty.setDeletedAt(LocalDate.now());
            facultyRepository.save(faculty);
    }
}