package com.tkpm.sms.service;

import com.tkpm.sms.dto.request.student.StudentCollectionRequest;
import com.tkpm.sms.dto.request.student.StudentCreateRequestDto;
import com.tkpm.sms.dto.request.student.StudentUpdateRequestDto;
import com.tkpm.sms.dto.response.student.StudentFileDto;
import com.tkpm.sms.entity.Student;
import com.tkpm.sms.exceptions.ApplicationException;
import com.tkpm.sms.exceptions.ErrorCode;
import com.tkpm.sms.mapper.AddressMapper;
import com.tkpm.sms.mapper.IdentityMapper;
import com.tkpm.sms.mapper.StudentMapper;
import com.tkpm.sms.repository.StudentRepository;
import com.tkpm.sms.specification.StudentSpecifications;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudentService {
    StudentRepository studentRepository;
    StatusService statusService;
    ProgramService programService;
    FacultyService facultyService;
    StudentMapper studentMapper;

    AddressService addressService;
    AddressMapper addressMapper;

    IdentityService identityService;
    IdentityMapper identityMapper;

    public Page<Student> findAll(StudentCollectionRequest search) {
        Pageable pageable = PageRequest.of(
                search.getPage() - 1,
                search.getSize(),
                Sort.by(
                        search.getSortDirection().equalsIgnoreCase("desc")
                                ? Sort.Direction.DESC : Sort.Direction.ASC,
                        search.getSortBy()
                ));

        return studentRepository.findAll(StudentSpecifications.withFilters(search), pageable);
    }

    public Student getStudentDetail(String id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND.withMessage(String.format("Student with id %s not found", id))));
    }

    @Transactional
    public Student createStudent(StudentCreateRequestDto studentCreateRequestDto) {
        if (studentRepository.existsStudentByStudentId(studentCreateRequestDto.getStudentId())) {
            throw new ApplicationException(ErrorCode.CONFLICT.withMessage(
                    String.format(
                            "Student with id %s already existed",
                            studentCreateRequestDto.getStudentId())));
        }

        if (studentRepository.existsStudentByEmail(studentCreateRequestDto.getEmail())) {
            throw new ApplicationException(ErrorCode.CONFLICT.withMessage(
                    String.format(
                            "Student with email %s already existed",
                            studentCreateRequestDto.getEmail())));
        }

        if (studentRepository.existsStudentByPhone(studentCreateRequestDto.getPhone())) {
            throw new ApplicationException(ErrorCode.CONFLICT.withMessage(
                    String.format(
                            "Student with phone number %s already existed",
                            studentCreateRequestDto.getPhone())));
        }

        Student student = studentMapper.createStudent(studentCreateRequestDto, facultyService, programService, statusService);

        log.info("Student created with address: {}", student.getPermanentAddress());

        //TODO: Refactor
        if (studentCreateRequestDto.getMailingAddress() != null) {
            var mailingAddress = addressService.createAddress(studentCreateRequestDto.getMailingAddress());
            student.setMailingAddress(mailingAddress);
        }

        if (studentCreateRequestDto.getPermanentAddress() != null) {
            var permanentAddress = addressService.createAddress(studentCreateRequestDto.getPermanentAddress());
            student.setPermanentAddress(permanentAddress);
        }

        if (studentCreateRequestDto.getTemporaryAddress() != null) {
            var temporaryAddress = addressService.createAddress(studentCreateRequestDto.getTemporaryAddress());
            student.setTemporaryAddress(temporaryAddress);
        }

        student.setIdentity(
                identityService.createIdentity(studentCreateRequestDto.getIdentity()));

        student = studentRepository.save(student);
        return student;
    }

    @Transactional
    public void saveListStudentFromFile(List<StudentFileDto> students) {
        var studentCreateRequests = students.stream().map(studentMapper::toStudentCreateRequest).toList();

        for (var student : studentCreateRequests) {
            createStudent(student);
        }
    }

    @Transactional
    public void deleteStudentById(String id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND.withMessage(String.format("Student with id %s not found", id))));

        studentRepository.delete(student);
    }

    @Transactional
    public Student updateStudent(String id, StudentUpdateRequestDto studentUpdateRequestDto) {
        var student = studentRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND.withMessage(String.format("Student with id %s not found", id))));

        if (!student.getEmail().equals(studentUpdateRequestDto.getEmail())
                && studentRepository.existsStudentByEmail(studentUpdateRequestDto.getEmail())) {

            var errorCode = ErrorCode.CONFLICT;
            errorCode.setMessage(
                    String.format("Student with email %s already existed",
                            studentUpdateRequestDto.getEmail()));

            throw new ApplicationException(errorCode);
        }

        if (!student.getPhone().equals(studentUpdateRequestDto.getPhone())
                && studentRepository.existsStudentByPhone(studentUpdateRequestDto.getPhone())) {
            throw new ApplicationException(
                    ErrorCode.CONFLICT.withMessage(
                            String.format(
                                    "Student with phone number %s already existed",
                                    studentUpdateRequestDto.getPhone())));
        }

        studentMapper.updateStudent(student, studentUpdateRequestDto, facultyService, programService, statusService);

        //TODO: Refactor
        if (studentUpdateRequestDto.getTemporaryAddress() != null) {
            if (student.getTemporaryAddress() == null) {
                student.setTemporaryAddress(
                        addressService.createAddress(
                                addressMapper.updateToCreateRequest(studentUpdateRequestDto.getTemporaryAddress())));
            } else {
                var newTemporaryAddress = addressService.updateAddress(
                        studentUpdateRequestDto.getTemporaryAddress(),
                        student.getTemporaryAddress().getId());
                student.setTemporaryAddress(newTemporaryAddress);
            }
        }

        if (studentUpdateRequestDto.getPermanentAddress() != null) {
            if (student.getPermanentAddress() == null) {
                student.setPermanentAddress(
                        addressService.createAddress(
                                addressMapper.updateToCreateRequest(studentUpdateRequestDto.getPermanentAddress())));
            } else {
                var newPermanentAddress = addressService.updateAddress(
                        studentUpdateRequestDto.getPermanentAddress(),
                        student.getPermanentAddress().getId());
                student.setPermanentAddress(newPermanentAddress);
            }
        }

        if (studentUpdateRequestDto.getMailingAddress() != null) {
            if (student.getMailingAddress() == null) {
                student.setMailingAddress(
                        addressService.createAddress(
                                addressMapper.updateToCreateRequest(studentUpdateRequestDto.getMailingAddress())));
            } else {
                var newMailingAddress = addressService.updateAddress(
                        studentUpdateRequestDto.getMailingAddress(),
                        student.getMailingAddress().getId());
                student.setMailingAddress(newMailingAddress);
            }
        }

        if (studentUpdateRequestDto.getIdentity() != null) {
            if (student.getIdentity() != null) {
                student.setIdentity(
                        identityService.updateIdentity(
                                studentUpdateRequestDto.getIdentity(),
                                student.getIdentity().getId()));
            } else {
                student.setIdentity(
                        identityService.createIdentity(
                                identityMapper.toIdentityCreateRequestDto(studentUpdateRequestDto.getIdentity())));
            }
        }

        student = studentRepository.save(student);

        return student;
    }
}
