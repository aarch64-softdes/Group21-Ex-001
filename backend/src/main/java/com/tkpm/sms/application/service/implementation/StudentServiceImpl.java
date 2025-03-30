package com.tkpm.sms.application.service.implementation;

import com.tkpm.sms.application.dto.request.student.StudentCollectionRequest;
import com.tkpm.sms.application.dto.request.student.StudentCreateRequestDto;
import com.tkpm.sms.application.dto.request.student.StudentUpdateRequestDto;
import com.tkpm.sms.application.dto.response.student.StudentFileDto;
import com.tkpm.sms.application.exception.ApplicationException;
import com.tkpm.sms.application.exception.ErrorCode;
import com.tkpm.sms.application.exception.ExceptionTranslator;
import com.tkpm.sms.application.mapper.AddressMapper;
import com.tkpm.sms.application.mapper.IdentityMapper;
import com.tkpm.sms.infrastructure.mapper.StudentMapperImpl;
import com.tkpm.sms.application.service.interfaces.*;
import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Address;
import com.tkpm.sms.domain.model.Student;
import com.tkpm.sms.domain.model.Identity;
import com.tkpm.sms.domain.exception.DomainException;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.repository.StudentRepository;
import com.tkpm.sms.domain.service.validators.AddressDomainValidator;
import com.tkpm.sms.domain.service.validators.FacultyDomainValidator;
import com.tkpm.sms.domain.service.validators.IdentityDomainValidator;
import com.tkpm.sms.domain.service.validators.ProgramDomainValidator;
import com.tkpm.sms.domain.service.validators.StatusDomainValidator;
import com.tkpm.sms.domain.service.validators.StudentDomainValidator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudentServiceImpl implements StudentService {
    StudentRepository studentRepository;

    StatusDomainValidator statusValidator;
    ProgramDomainValidator programValidator;
    FacultyDomainValidator facultyValidator;
    AddressDomainValidator addressValidator;
    IdentityDomainValidator identityValidator;
    StudentDomainValidator studentValidator;

    StatusService statusService;
    ProgramService programService;
    FacultyService facultyService;
    IdentityService identityService;

    StudentMapperImpl studentMapper;
    AddressMapper addressMapper;
    IdentityMapper identityMapper;

    ExceptionTranslator exceptionTranslator;
    PhoneParser phoneParser;

    @Override
    public PageResponse<Student> findAll(StudentCollectionRequest search) {
        PageRequest pageRequest = PageRequest.builder()
                .pageNumber(search.getPage())
                .pageSize(search.getSize())
                .sortBy(search.getSortBy())
                .sortDirection("desc".equalsIgnoreCase(search.getSortDirection())
                        ? PageRequest.SortDirection.DESC
                        : PageRequest.SortDirection.ASC)
                .build();

        return studentRepository.findWithFilters(search.getSearch(), search.getFaculty(), pageRequest);
    }

    @Override
    public Student getStudentDetail(String id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(
                        ErrorCode.NOT_FOUND.withMessage(
                                String.format("Student with id %s not found", id)
                        )));
    }

    @Override
    @Transactional
    public Student createStudent(StudentCreateRequestDto requestDto) {
        try {
            // Validate student fields
            studentValidator.validateStudentIdUniqueness(requestDto.getStudentId());
            studentValidator.validateEmailUniqueness(requestDto.getEmail());
            studentValidator.validateName(requestDto.getName());
            studentValidator.validateEmail(requestDto.getEmail());

            // Parse and validate phone
            String phoneNumber = phoneParser.parsePhoneNumber(
                    requestDto.getPhone().getPhoneNumber(),
                    requestDto.getPhone().getCountryCode());

            // Convert DTO to domain entity
            Student student = studentMapper.toStudent(requestDto);
            student.setPhone(phoneNumber);

            // Set related entities
            var faculty = facultyService.getFacultyByName(requestDto.getFaculty());
            var program = programService.getProgramByName(requestDto.getProgram());
            var status = statusService.getStatusByName(requestDto.getStatus());

            student.setFaculty(faculty);
            student.setProgram(program);
            student.setStatus(status);

            // Handle addresses
            if (requestDto.getPermanentAddress() != null) {
                Address address = addressMapper.toAddress(requestDto.getPermanentAddress());
                addressValidator.validateAddressFields(address);
                student.setPermanentAddress(address);
            }

            if (requestDto.getTemporaryAddress() != null) {
                Address address = addressMapper.toAddress(requestDto.getTemporaryAddress());
                addressValidator.validateAddressFields(address);
                student.setTemporaryAddress(address);
            }

            if (requestDto.getMailingAddress() != null) {
                Address address = addressMapper.toAddress(requestDto.getMailingAddress());
                addressValidator.validateAddressFields(address);
                student.setMailingAddress(address);
            }

            // Handle identity
            if (requestDto.getIdentity() != null) {
                Identity identity = identityMapper.toIdentity(requestDto.getIdentity());
                identityValidator.validateIdentityNumber(identity.getType(), identity.getNumber());
                identityValidator.validateIssuedDateBeforeExpiryDate(
                        identity.getIssuedDate(), identity.getExpiryDate());
                identityValidator.validateIdentityUniqueness(identity.getType(), identity.getNumber());
                student.setIdentity(identity);
            }

            // Save student
            return studentRepository.save(student);
        } catch (DomainException e) {
            throw exceptionTranslator.translateException(e);
        }
    }

    @Override
    @Transactional
    public Student updateStudent(String id, StudentUpdateRequestDto requestDto) {
        try {
            // Find existing student
            Student student = studentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("Student with id %s not found", id)));

            // Validate student fields
            if (!student.getStudentId().equals(requestDto.getStudentId())) {
                studentValidator.validateStudentIdUniquenessForUpdate(requestDto.getStudentId(), id);
            }

            if (!student.getEmail().equals(requestDto.getEmail())) {
                studentValidator.validateEmailUniquenessForUpdate(requestDto.getEmail(), id);
                studentValidator.validateEmail(requestDto.getEmail());
            }

            studentValidator.validateName(requestDto.getName());

            // Parse and validate phone
            String phoneNumber = phoneParser.parsePhoneNumber(
                    requestDto.getPhone().getPhoneNumber(),
                    requestDto.getPhone().getCountryCode());

            // Update student fields
            studentMapper.updateStudentFromDto(requestDto, student);
            student.setPhone(phoneNumber);

            // Update related entities
            var faculty = facultyService.getFacultyByName(requestDto.getFaculty());
            var program = programService.getProgramByName(requestDto.getProgram());
            var newStatus = statusService.getStatusByName(requestDto.getStatus());

            // Validate status transition
            if (student.getStatus() != null && !student.getStatus().getId().equals(newStatus.getId())) {
                studentValidator.validateStatusTransition(student, newStatus);
            }

            student.setFaculty(faculty);
            student.setProgram(program);
            student.setStatus(newStatus);

            // Update addresses
            updateAddresses(student, requestDto);

            // Update identity
            updateIdentity(student, requestDto);

            // Save updated student
            return studentRepository.save(student);
        } catch (DomainException e) {
            throw exceptionTranslator.translateException(e);
        }
    }

    private void updateAddresses(Student student, StudentUpdateRequestDto requestDto) {
        // Update permanent address
        if (requestDto.getPermanentAddress() != null) {
            if (student.getPermanentAddress() == null) {
                Address address = addressMapper.toAddress(
                        addressMapper.updateToCreateRequest(requestDto.getPermanentAddress()));
                addressValidator.validateAddressFields(address);
                student.setPermanentAddress(address);
            } else {
                addressMapper.updateAddressFromDto(
                        requestDto.getPermanentAddress(), student.getPermanentAddress());
                addressValidator.validateAddressFields(student.getPermanentAddress());
            }
        }

        // Update temporary address
        if (requestDto.getTemporaryAddress() != null) {
            if (student.getTemporaryAddress() == null) {
                Address address = addressMapper.toAddress(
                        addressMapper.updateToCreateRequest(requestDto.getTemporaryAddress()));
                addressValidator.validateAddressFields(address);
                student.setTemporaryAddress(address);
            } else {
                addressMapper.updateAddressFromDto(
                        requestDto.getTemporaryAddress(), student.getTemporaryAddress());
                addressValidator.validateAddressFields(student.getTemporaryAddress());
            }
        }

        // Update mailing address
        if (requestDto.getMailingAddress() != null) {
            if (student.getMailingAddress() == null) {
                Address address = addressMapper.toAddress(
                        addressMapper.updateToCreateRequest(requestDto.getMailingAddress()));
                addressValidator.validateAddressFields(address);
                student.setMailingAddress(address);
            } else {
                addressMapper.updateAddressFromDto(
                        requestDto.getMailingAddress(), student.getMailingAddress());
                addressValidator.validateAddressFields(student.getMailingAddress());
            }
        }
    }

    private void updateIdentity(Student student, StudentUpdateRequestDto requestDto) {
        if (requestDto.getIdentity() != null) {
            if (student.getIdentity() == null) {
                Identity identity = identityMapper.toIdentity(
                        identityMapper.toCreateDto(requestDto.getIdentity()));
                identityValidator.validateIdentityNumber(identity.getType(), identity.getNumber());
                identityValidator.validateIssuedDateBeforeExpiryDate(
                        identity.getIssuedDate(), identity.getExpiryDate());
                identityValidator.validateIdentityUniqueness(identity.getType(), identity.getNumber());
                student.setIdentity(identity);
            } else {
                Identity identity = student.getIdentity();
                String oldType = identity.getType().getDisplayName();
                String oldNumber = identity.getNumber();

                identityMapper.updateIdentityFromDto(requestDto.getIdentity(), identity);

                // Only validate uniqueness if type or number changed
                if (!oldType.equals(identity.getType().getDisplayName()) || !oldNumber.equals(identity.getNumber())) {
                    identityValidator.validateIdentityUniquenessForUpdate(
                            identity.getType(), identity.getNumber(), identity.getId());
                }

                identityValidator.validateIdentityNumber(identity.getType(), identity.getNumber());
                identityValidator.validateIssuedDateBeforeExpiryDate(
                        identity.getIssuedDate(), identity.getExpiryDate());
            }
        }
    }

    @Override
    @Transactional
    public void deleteStudentById(String id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(
                        ErrorCode.NOT_FOUND.withMessage(
                                String.format("Student with id %s not found", id)
                        )));

        studentRepository.delete(student);
    }

    @Override
    @Transactional
    public void saveListStudentFromFile(List<StudentFileDto> studentFileDtos) {
        List<StudentCreateRequestDto> studentCreateRequests = studentFileDtos.stream()
                .map(studentMapper::toStudentCreateRequest)
                .collect(Collectors.toList());

        for (StudentCreateRequestDto request : studentCreateRequests) {
            createStudent(request);
        }
    }
}