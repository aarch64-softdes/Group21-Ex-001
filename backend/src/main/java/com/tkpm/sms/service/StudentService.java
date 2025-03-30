package com.tkpm.sms.service;

import com.tkpm.sms.application.service.interfaces.FacultyService;
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
import com.tkpm.sms.utils.PhoneParser;
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
    SettingService settingService;
    FacultyService facultyService;
    AddressService addressService;
    IdentityService identityService;

    StudentMapper studentMapper;
    AddressMapper addressMapper;
    IdentityMapper identityMapper;

    PhoneParser phoneParser;

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
        Student student = studentMapper.toEntity(studentCreateRequestDto);

        var phoneNumberRequest = phoneParser.parsePhoneNumber(studentCreateRequestDto.getPhone().getPhoneNumber(),
                studentCreateRequestDto.getPhone().getCountryCode());
        student.setPhone(phoneNumberRequest);
        student.setMailingAddress(addressMapper.toEntity(studentCreateRequestDto.getMailingAddress()));
        student.setPermanentAddress(addressMapper.toEntity(studentCreateRequestDto.getPermanentAddress()));
        student.setTemporaryAddress(addressMapper.toEntity(studentCreateRequestDto.getTemporaryAddress()));
        student.setIdentity(identityMapper.toEntity(studentCreateRequestDto.getIdentity()));

        if (identityService.cannotCreateIdentity(student.getIdentity())){
            throw new ApplicationException(ErrorCode.CONFLICT.withMessage(
                    String.format(
                            "Student with the %s and number %s already existed",
                            student.getIdentity().getType(),
                            student.getIdentity().getNumber())));
        }

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

        validateEmailDomain(studentCreateRequestDto.getEmail());

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
    public Student updateStudent(String id, StudentUpdateRequestDto updateRequestDto) {
        // Find existing student
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND.withMessage(String.format("Student with id %s not found", id))));

        // Parse phone number from request
        var phoneNumberRequest = phoneParser.parsePhoneNumber(updateRequestDto.getPhone().getPhoneNumber(),
                updateRequestDto.getPhone().getCountryCode());

        // Update student base fields
        studentMapper.updateStudent(student, updateRequestDto);
        student.setPhone(phoneNumberRequest);

        // Update temporary address
        if (updateRequestDto.getTemporaryAddress() != null) {
            if (student.getTemporaryAddress() == null) {
                student.setTemporaryAddress(
                        addressService.createAddress(
                                addressMapper.updateToCreateRequest(updateRequestDto.getTemporaryAddress())));
            } else {
                student.setTemporaryAddress(
                        addressService.updateAddress(
                                updateRequestDto.getTemporaryAddress(),
                                student.getTemporaryAddress().getId()));
            }
        }

        // Update permanent address
        if (updateRequestDto.getPermanentAddress() != null) {
            if (student.getPermanentAddress() == null) {
                student.setPermanentAddress(
                        addressService.createAddress(
                                addressMapper.updateToCreateRequest(updateRequestDto.getPermanentAddress())));
            } else {
                student.setPermanentAddress(
                        addressService.updateAddress(
                                updateRequestDto.getPermanentAddress(),
                                student.getPermanentAddress().getId()));
            }
        }

        // Update mailing address
        if (updateRequestDto.getMailingAddress() != null) {
            if (student.getMailingAddress() == null) {
                student.setMailingAddress(
                        addressService.createAddress(
                                addressMapper.updateToCreateRequest(updateRequestDto.getMailingAddress())));
            } else {
                student.setMailingAddress(
                        addressService.updateAddress(
                                updateRequestDto.getMailingAddress(),
                                student.getMailingAddress().getId()));
            }
        }

        if (updateRequestDto.getIdentity() != null) {
            if (student.getIdentity() != null) {
                student.setIdentity(
                        identityService.updateIdentity(
                                updateRequestDto.getIdentity(),
                                student.getIdentity().getId()));
            } else {
                student.setIdentity(
                        identityService.createIdentity(
                                identityMapper.toCreateDto(updateRequestDto.getIdentity())));
            }
        }

        // Validate email domain
        validateEmailDomain(updateRequestDto.getEmail());

        // Validate student ID uniqueness
        if (!student.getStudentId().equals(updateRequestDto.getStudentId())
                && studentRepository.existsStudentByStudentId(updateRequestDto.getStudentId())) {
                throw new ApplicationException(ErrorCode.CONFLICT.withMessage(
                        String.format("Student with id %s already exists", updateRequestDto.getStudentId())));
        }

        // Validate email uniqueness
        if (!student.getEmail().equals(updateRequestDto.getEmail())
                && studentRepository.existsStudentByEmail(updateRequestDto.getEmail())) {
            throw new ApplicationException(ErrorCode.CONFLICT.withMessage(
                    String.format("Student with email %s already existed", updateRequestDto.getEmail())));
        }

        // Validate status transition
        if (updateRequestDto.getStatus() != null) {
            var fromStatus = student.getStatus();
            var toStatus = statusService.getStatusByName(updateRequestDto.getStatus());

            if (!fromStatus.equals(toStatus)) {
                if (!statusService.isTransitionAllowed(fromStatus.getId(), toStatus.getId())) {
                    throw new ApplicationException(ErrorCode.UNSUPPORTED_STATUS_TRANSITION.withMessage(
                            String.format("Transition from %s to %s is not allowed",
                                    fromStatus.getName(), toStatus.getName())));
                }
                student.setStatus(toStatus);
                log.info("Transition from {} to {} is allowed", fromStatus.getName(), toStatus.getName());
            } else {
                log.info("No need to change status");
            }
        }

        return studentRepository.save(student);
    }

    @Transactional
    public void deleteStudentById(String id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND.withMessage(String.format("Student with id %s not found", id))));

        studentRepository.delete(student);
    }

    private void validateEmailDomain(String studentEmail) {
        var getStudentDomain = studentEmail.substring(studentEmail.indexOf(SettingService.AT_SIGN));
        var validDomain = settingService.getEmailSetting().getDetails();
        log.info("Valid domain: {}", validDomain);
        log.info("Student domain: {}", getStudentDomain);

        if (validDomain.isEmpty()) {
            throw new ApplicationException(ErrorCode.NOT_FOUND.withMessage(String.format("Student with email %s not found", studentEmail)));
        }

        if (!validDomain.equals(getStudentDomain)) {
            throw new ApplicationException(ErrorCode.UNSUPPORTED_EMAIL_DOMAIN.withMessage("Email domain is not supported, only " + validDomain + " is allowed"));
        }
    }
}
