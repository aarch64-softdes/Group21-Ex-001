package com.tkpm.sms.application.service.implementation;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.faculty.FacultyRequestDto;
import com.tkpm.sms.application.mapper.FacultyMapper;
import com.tkpm.sms.application.service.interfaces.FacultyService;
import com.tkpm.sms.application.service.interfaces.TextContentService;
import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.Faculty;
import com.tkpm.sms.domain.repository.FacultyRepository;
import com.tkpm.sms.domain.service.TranslatorService;
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
    FacultyMapper facultyMapper;
    TextContentService textContentService;
    TranslatorService translatorService;

    @Override
    public PageResponse<Faculty> getAllFaculties(BaseCollectionRequest search) {
        PageRequest pageRequest = PageRequest.from(search);
        return facultyRepository.findAll(pageRequest);
    }

    @Override
    public Faculty getFacultyById(Integer id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.not_found.id",
                        translatorService.getEntityTranslatedName(Faculty.class), id));
    }

    @Override
    public Faculty getFacultyByName(String name) {
        return facultyRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("error.not_found.name",
                        translatorService.getEntityTranslatedName(Faculty.class), name));
    }

    @Override
    @Transactional
    public Faculty createFaculty(FacultyRequestDto facultyRequest) {
        // Use domain service for business validation
        facultyValidator.validateNameUniqueness(facultyRequest.getName());

        var faculty = facultyMapper.toDomain(facultyRequest);
        faculty.setName(textContentService.createTextContent(facultyRequest.getName()));

        return facultyRepository.save(faculty);
    }

    @Override
    @Transactional
    public Faculty updateFaculty(Integer id, FacultyRequestDto facultyRequest) {
        // Use domain service for business validation
        facultyValidator.validateNameUniquenessForUpdate(facultyRequest.getName(), id);

        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.not_found.id",
                        translatorService.getEntityTranslatedName(Faculty.class), id));

        facultyMapper.toDomain(facultyRequest, faculty);
        faculty.setName(
                textContentService.updateTextContent(faculty.getName(), facultyRequest.getName()));

        return facultyRepository.save(faculty);
    }

    @Override
    @Transactional
    public void deleteFaculty(Integer id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.not_found.id",
                        translatorService.getEntityTranslatedName(Faculty.class), id));

        facultyRepository.delete(faculty);
    }
}