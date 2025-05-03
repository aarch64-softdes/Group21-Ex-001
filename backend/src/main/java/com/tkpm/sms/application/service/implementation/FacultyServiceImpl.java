package com.tkpm.sms.application.service.implementation;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.faculty.FacultyRequestDto;
import com.tkpm.sms.application.service.interfaces.FacultyService;
import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.Faculty;
import com.tkpm.sms.domain.repository.FacultyRepository;
import com.tkpm.sms.domain.service.validators.FacultyDomainValidator;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FacultyServiceImpl implements FacultyService {
    FacultyRepository facultyRepository;
    FacultyDomainValidator facultyValidator;

    @Override
    public PageResponse<Faculty> getAllFaculties(BaseCollectionRequest search) {
        PageRequest pageRequest = PageRequest.from(search);
        return facultyRepository.findAll(pageRequest);
    }

    @Override
    public Faculty getFacultyById(Integer id) {
        return facultyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Faculty with id %s not found", id)));
    }

    @Override
    public Faculty getFacultyByName(String name) {
        return facultyRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Faculty with name %s not found", name)));
    }

    @Override
    @Transactional
    public Faculty createFaculty(FacultyRequestDto faculty) {
        // Use domain service for business validation
        facultyValidator.validateNameUniqueness(faculty.getName());

        var newFaculty = Faculty.builder().name(faculty.getName()).build();
        return facultyRepository.save(newFaculty);
    }

    @Override
    @Transactional
    public Faculty updateFaculty(Integer id, FacultyRequestDto faculty) {
        // Use domain service for business validation
        facultyValidator.validateNameUniquenessForUpdate(faculty.getName(), id);

        Faculty facultyToUpdate = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Faculty with id %s not found", id)));

        facultyToUpdate.setName(faculty.getName());
        return facultyRepository.save(facultyToUpdate);
    }

    @Override
    @Transactional
    public void deleteFaculty(Integer id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Faculty with id %s not found", id)));

        facultyRepository.delete(faculty);
    }
}