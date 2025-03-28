package com.tkpm.sms.service;

import com.tkpm.sms.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.dto.request.faculty.FacultyRequestDto;
import com.tkpm.sms.entity.Faculty;
import com.tkpm.sms.exceptions.ApplicationException;
import com.tkpm.sms.exceptions.ErrorCode;
import com.tkpm.sms.repository.FacultyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FacultyService {
    FacultyRepository facultyRepository;

    public Page<Faculty> getAllFaculties(BaseCollectionRequest search) {
        Pageable pageable = PageRequest.of(
                search.getPage() - 1,
                search.getSize(),
                Sort.by(
                        search.getSortDirection().equalsIgnoreCase("desc")
                                ? Sort.Direction.DESC : Sort.Direction.ASC,
                        search.getSortBy()
                ));
        return facultyRepository.findAll(pageable);
    }

    public Faculty getFacultyById(Integer id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(
                        ErrorCode.NOT_FOUND.withMessage(
                                String.format("Faculty with id %s not found", id)
                        )));
    }

    public Faculty getFacultyByName(String name) {
        return facultyRepository.findFacultyByName(name)
                .orElseThrow(() -> new ApplicationException(
                        ErrorCode.NOT_FOUND.withMessage(
                                String.format("Faculty with name %s not found", name)
                        )));
    }

    @Transactional
    public Faculty createFaculty(FacultyRequestDto faculty) {
        if (facultyRepository.existsFacultyByName(faculty.getName())) {
            throw new ApplicationException(
                    ErrorCode.CONFLICT.withMessage(
                            String.format("Faculty with name %s already existed", faculty.getName())
                    ));
        }

        var newFaculty = Faculty.builder().name(faculty.getName()).build();

        return facultyRepository.save(newFaculty);
    }

    @Transactional
    public Faculty updateFaculty(Integer id, FacultyRequestDto faculty) {
        if (facultyRepository.existsFacultyByName(faculty.getName())) {
            throw new ApplicationException(
                    ErrorCode.CONFLICT.withMessage(
                            String.format("Faculty with name %s already existed", faculty.getName())
                    ));
        }

        Faculty facultyToUpdate = facultyRepository.findById(id).orElseThrow(
                () -> new ApplicationException(
                        ErrorCode.NOT_FOUND.withMessage(
                                String.format("Faculty with id %s not found", id)
                        )));
        facultyToUpdate.setName(faculty.getName());

        return facultyRepository.save(facultyToUpdate);
    }

    @Transactional
    public void deleteFaculty(Integer id) {
        var faculty = facultyRepository.findById(id).orElseThrow(
                () -> new ApplicationException(
                        ErrorCode.NOT_FOUND.withMessage(
                                String.format("Faculty with id %s not found", id)
                        )));
        faculty.setDeletedAt(LocalDate.now());

        facultyRepository.save(faculty);
    }
}
