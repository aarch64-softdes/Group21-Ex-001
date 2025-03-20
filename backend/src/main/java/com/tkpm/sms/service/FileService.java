package com.tkpm.sms.service;

import com.tkpm.sms.dto.response.student.StudentFileDto;
import com.tkpm.sms.entity.Student;
import com.tkpm.sms.factories.FileStrategyFactory;
import com.tkpm.sms.mapper.StudentMapper;
import com.tkpm.sms.repository.StudentRepository;
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
public class FileService {
    StudentRepository studentRepository;
    StudentMapper studentMapper;
    FileStrategyFactory fileStrategyFactory;

    public byte[] exportStudentFile(String format) {
        List<StudentFileDto> students = new ArrayList<>();
        int pageSize = 100;
        int currentPage = 0;
        Page<Student> studentPage;
        do {
            studentPage = studentRepository.findAll(
                    PageRequest.of(currentPage, pageSize, Sort.by("studentId"))
            );
            students.addAll(studentPage.getContent().stream()
                    .map(studentMapper::toStudentFileDto)
                    .toList());
            currentPage++;
        } while (currentPage < studentPage.getTotalPages());

        return fileStrategyFactory.getStrategy(format).export(students);
    }

    public void importStudentFile(String format, MultipartFile multipartFile) {
        fileStrategyFactory.getStrategy(format).importFile(multipartFile);
    }
}
