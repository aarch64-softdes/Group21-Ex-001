package com.tkpm.sms.infrastructure.service;

import com.tkpm.sms.application.dto.response.student.StudentFileDto;
import com.tkpm.sms.application.mapper.StudentMapper;
import com.tkpm.sms.application.service.interfaces.FileService;
import com.tkpm.sms.domain.exception.FileProcessingException;
import com.tkpm.sms.infrastructure.factories.FileStrategyFactory;
import com.tkpm.sms.infrastructure.persistence.entity.StudentEntity;
import com.tkpm.sms.infrastructure.persistence.jpa.StudentJpaRepository;
import com.tkpm.sms.infrastructure.persistence.mapper.StudentPersistenceMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileServiceImpl implements FileService {
    StudentJpaRepository studentRepository;
    StudentPersistenceMapper studentPersistenceMapper;
    StudentMapper studentMapper;
    FileStrategyFactory fileStrategyFactory;

    @Override
    public byte[] exportStudentFile(String format) {
        List<StudentFileDto> students = new ArrayList<>();
        int pageSize = 100;
        int currentPage = 0;
        Page<StudentEntity> studentPage;

        do {
            studentPage = studentRepository.findAll(
                    PageRequest.of(
                            currentPage,
                            pageSize,
                            Sort.by("studentId")));

            students.addAll(studentPage
            .getContent()
            .stream()
            .map(studentPersistenceMapper::toDomain)
            .map(studentMapper::toStudentFileDto)
            .toList());

            currentPage++;
        } while (currentPage < studentPage.getTotalPages());

        return fileStrategyFactory.getStrategy(format).export(students);
    }

    @Override
    public void importStudentFile(String format, Object multipartFile) {
        if (!(multipartFile instanceof MultipartFile)) {
            throw new FileProcessingException("Invalid file type");
        }

        fileStrategyFactory.getStrategy(format).importFile(multipartFile);
    }
}