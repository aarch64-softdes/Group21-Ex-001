package com.tkpm.sms.application.service.implementation;

import com.tkpm.sms.application.dto.request.student.StudentCollectionRequest;
import com.tkpm.sms.application.dto.request.student.StudentCreateRequestDto;
import com.tkpm.sms.application.dto.request.student.StudentUpdateRequestDto;
import com.tkpm.sms.application.dto.response.student.StudentFileDto;
import com.tkpm.sms.application.mapper.AddressMapper;
import com.tkpm.sms.application.mapper.IdentityMapper;
import com.tkpm.sms.application.service.interfaces.*;
import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.exception.ErrorCode;
import com.tkpm.sms.domain.exception.FileProcessingException;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.*;
import com.tkpm.sms.domain.repository.StudentRepository;
import com.tkpm.sms.domain.service.TranslatorService;
import com.tkpm.sms.domain.service.validators.IdentityDomainValidator;
import com.tkpm.sms.domain.service.validators.StudentDomainValidator;
import com.tkpm.sms.domain.valueobject.Phone;
import com.tkpm.sms.infrastructure.mapper.StudentMapperImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudentServiceImpl implements StudentService {
    StudentRepository studentRepository;

    IdentityDomainValidator identityValidator;
    StudentDomainValidator studentValidator;

    StatusService statusService;
    ProgramService programService;
    FacultyService facultyService;

    StudentMapperImpl studentMapper;
    AddressMapper addressMapper;
    IdentityMapper identityMapper;

    PhoneParser phoneParser;

    TranslatorService translatorService;

    @Override
    public PageResponse<Student> findAll(StudentCollectionRequest search) {
        PageRequest pageRequest = PageRequest.from(search);

        return studentRepository.findWithFilters(search.getSearch(), search.getFaculty(),
                pageRequest);
    }

    @Override
    public Student getStudentDetail(String id, String languageCode) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.not_found.id",
                        translatorService.getEntityTranslatedName(Student.class), id));
    }

    @Override
    @Transactional
    public Student createStudent(StudentCreateRequestDto requestDto, String languageCode) {
        // Validate student fields
        studentValidator.validateStudentIdUniqueness(requestDto.getStudentId());
        studentValidator.validateEmailUniqueness(requestDto.getEmail());
        studentValidator.validateEmailDomain(requestDto.getEmail());

        // Parse and validate phone
        Phone phoneNumber = phoneParser.parsePhoneNumberToPhone(
                requestDto.getPhone().getPhoneNumber(), requestDto.getPhone().getCountryCode());

        // Convert DTO to domain entity
        Student student = studentMapper.toStudent(requestDto);
        student.setPhone(phoneNumber);

        // Set related entities
        var faculty = facultyService.getFacultyById(requestDto.getFacultyId());
        var program = programService.getProgramById(requestDto.getProgramId());
        var status = statusService.getStatusById(requestDto.getStatusId());

        student.setFaculty(faculty);
        student.setProgram(program);
        student.setStatus(status);

        // Handle addresses
        handleStudentAddresses(student, requestDto);
        handleStudentIdentity(student, requestDto);

        // Save student
        return studentRepository.save(student);
    }

    @Override
    @Transactional
    public Student updateStudent(String id, StudentUpdateRequestDto requestDto,
            String languageCode) {
        // Find existing student
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.not_found.id",
                        translatorService.getEntityTranslatedName(Student.class), id));

        // Validate student fields
        if (!student.getStudentId().equals(requestDto.getStudentId())) {
            studentValidator.validateStudentIdUniquenessForUpdate(requestDto.getStudentId(), id);
        }

        if (!student.getEmail().equals(requestDto.getEmail())) {
            studentValidator.validateEmailUniquenessForUpdate(requestDto.getEmail(), id);
        }

        studentValidator.validateEmailDomain(requestDto.getEmail());
        // Parse and validate phone
        Phone phoneNumber = phoneParser.parsePhoneNumberToPhone(
                requestDto.getPhone().getPhoneNumber(), requestDto.getPhone().getCountryCode());

        // Update student fields
        studentMapper.updateStudentFromDto(requestDto, student);
        student.setPhone(phoneNumber);

        // Update related entities
        var faculty = facultyService.getFacultyById(requestDto.getFacultyId());
        var program = programService.getProgramById(requestDto.getProgramId());
        var newStatus = statusService.getStatusById(requestDto.getStatusId());

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
    }

    private void updateAddresses(Student student, StudentUpdateRequestDto requestDto) {
        // Update permanent address
        if (requestDto.getPermanentAddress() != null) {
            if (student.getPermanentAddress() == null) {
                Address address = addressMapper.toAddress(
                        addressMapper.updateToCreateRequest(requestDto.getPermanentAddress()));
                student.setPermanentAddress(address);
            } else {
                addressMapper.updateAddressFromDto(requestDto.getPermanentAddress(),
                        student.getPermanentAddress());
            }
        } else {
            student.setPermanentAddress(null);
        }

        // Update temporary address
        if (requestDto.getTemporaryAddress() != null) {
            if (student.getTemporaryAddress() == null) {
                Address address = addressMapper.toAddress(
                        addressMapper.updateToCreateRequest(requestDto.getTemporaryAddress()));
                student.setTemporaryAddress(address);
            } else {
                addressMapper.updateAddressFromDto(requestDto.getTemporaryAddress(),
                        student.getTemporaryAddress());
            }
        } else {
            student.setTemporaryAddress(null);
        }

        // Update mailing address
        if (requestDto.getMailingAddress() != null) {
            if (student.getMailingAddress() == null) {
                Address address = addressMapper.toAddress(
                        addressMapper.updateToCreateRequest(requestDto.getMailingAddress()));
                student.setMailingAddress(address);
            } else {
                addressMapper.updateAddressFromDto(requestDto.getMailingAddress(),
                        student.getMailingAddress());
            }
        } else {
            student.setMailingAddress(null);
        }
    }

    /**
     * Assumes that the student has an identity
     */
    private void updateIdentity(Student student, StudentUpdateRequestDto requestDto) {
        Identity identity = student.getIdentity();
        String oldType = identity.getType().getDisplayName();
        String oldNumber = identity.getNumber();

        identityMapper.updateIdentityFromDto(requestDto.getIdentity(), identity);

        // Only validate uniqueness if the type or number changed
        if (!oldType.equals(identity.getType().getDisplayName())
                || !oldNumber.equals(identity.getNumber())) {
            identityValidator.validateIdentityUniquenessForUpdate(identity.getType(),
                    identity.getNumber(), identity.getId());
        }

    }

    @Override
    @Transactional
    public void deleteStudentById(String id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.not_found.id",
                        translatorService.getEntityTranslatedName(Student.class), id));

        studentRepository.delete(student);
    }

    @Override
    @Transactional
    public void saveListStudentFromFile(List<StudentFileDto> studentFileDtos) {
        Map<String, Faculty> facultyNameCache = createFacultyNameCache(studentFileDtos);
        Map<String, Status> statusNameCache = createStatusNameCache(studentFileDtos);
        Map<String, Program> programNameCache = createProgramNameCache(studentFileDtos);

        List<Student> validStudents = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        for (StudentFileDto dto : studentFileDtos) {
            try {
                StudentCreateRequestDto createRequest = studentMapper.toStudentCreateRequest(dto);
                Student student = studentMapper.toStudent(createRequest);

                if (!facultyNameCache.containsKey(dto.getFaculty())) {
                    throw new ResourceNotFoundException("error.not_found.name",
                            translatorService.getEntityTranslatedName(Faculty.class),
                            dto.getFaculty());
                }
                student.setFaculty(facultyNameCache.get(dto.getFaculty()));

                if (!statusNameCache.containsKey(dto.getStatus())) {
                    throw new ResourceNotFoundException("error.not_found.name",
                            translatorService.getEntityTranslatedName(Status.class),
                            dto.getStatus());
                }
                student.setStatus(statusNameCache.get(dto.getStatus()));

                if (!programNameCache.containsKey(dto.getProgram())) {
                    throw new ResourceNotFoundException("error.not_found.name",
                            translatorService.getEntityTranslatedName(Program.class),
                            dto.getProgram());
                }
                student.setProgram(programNameCache.get(dto.getProgram()));

                // Parse phone number
                if (createRequest.getPhone() != null) {
                    Phone phone = phoneParser.parsePhoneNumberToPhone(
                            createRequest.getPhone().getPhoneNumber(),
                            createRequest.getPhone().getCountryCode());
                    student.setPhone(phone);
                }

                handleStudentAddresses(student, createRequest);
                handleStudentIdentity(student, createRequest);

                // Basic validation
                studentValidator.validateStudentIdUniqueness(student.getStudentId());
                studentValidator.validateEmailUniqueness(student.getEmail());
                studentValidator.validateEmailDomain(student.getEmail());

                validStudents.add(student);
            } catch (Exception e) {
                errors.add(String.format("Error processing student %s: %s", dto.getStudentId(),
                        e.getMessage()));
                log.error("Error processing student {}: {}", dto.getStudentId(), e.getMessage());
            }
        }

        if (!validStudents.isEmpty()) {
            studentRepository.saveAll(validStudents);
            log.info("Imported {} students successfully", validStudents.size());
        }

        if (!errors.isEmpty()) {
            log.error("Failed to import students: {}", String.join("; ", errors));

            throw new FileProcessingException(ErrorCode.FAIL_TO_IMPORT_FILE,
                    "error.file_processing.failed_import_students");
        }
    }

    private void handleStudentAddresses(Student student, StudentCreateRequestDto requestDto) {
        if (requestDto.getPermanentAddress() != null) {
            Address address = addressMapper.toAddress(requestDto.getPermanentAddress());
            student.setPermanentAddress(address);
        }

        if (requestDto.getTemporaryAddress() != null) {
            Address address = addressMapper.toAddress(requestDto.getTemporaryAddress());
            student.setTemporaryAddress(address);
        }

        if (requestDto.getMailingAddress() != null) {
            Address address = addressMapper.toAddress(requestDto.getMailingAddress());
            student.setMailingAddress(address);
        }
    }

    private void handleStudentIdentity(Student student, StudentCreateRequestDto requestDto) {
        if (requestDto.getIdentity() != null) {
            Identity identity = identityMapper.toIdentity(requestDto.getIdentity());
            identityValidator.validateIdentityUniqueness(identity.getType(), identity.getNumber());
            student.setIdentity(identity);
        }
    }

    private Map<String, Faculty> createFacultyNameCache(List<StudentFileDto> dtos) {
        Set<String> facultyNames = dtos.stream().map(StudentFileDto::getFaculty)
                .filter(Objects::nonNull).collect(Collectors.toSet());

        Map<String, Faculty> result = new HashMap<>();
        for (String name : facultyNames) {
            try {
                log.info("Searching for faculty: {}", name);
                result.put(name, facultyService.getFacultyByName(name));
            } catch (Exception e) {
                log.warn("Faculty not found: {}", name);
            }
        }
        return result;
    }

    private Map<String, Status> createStatusNameCache(List<StudentFileDto> dtos) {
        Set<String> statusNames = dtos.stream().map(StudentFileDto::getStatus)
                .filter(Objects::nonNull).collect(Collectors.toSet());

        Map<String, Status> result = new HashMap<>();
        for (String name : statusNames) {
            try {
                log.info("Searching for status: {}", name);
                result.put(name, statusService.getStatusByName(name));
            } catch (Exception e) {
                log.warn("Status not found: {}", name);
            }
        }
        return result;
    }

    private Map<String, Program> createProgramNameCache(List<StudentFileDto> dtos) {
        Set<String> programNames = dtos.stream().map(StudentFileDto::getProgram)
                .filter(Objects::nonNull).collect(Collectors.toSet());

        Map<String, Program> result = new HashMap<>();
        for (String name : programNames) {
            try {
                log.info("Searching for program: {}", name);
                result.put(name, programService.getProgramByName(name));
            } catch (Exception e) {
                log.warn("Program not found: {}", name);
            }
        }
        return result;
    }
}