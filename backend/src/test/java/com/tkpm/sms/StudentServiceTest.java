package com.tkpm.sms;

import com.tkpm.sms.application.dto.request.identity.IdentityUpdateRequestDto;
import com.tkpm.sms.application.dto.request.phone.PhoneRequestDto;
import com.tkpm.sms.application.dto.request.student.StudentCreateRequestDto;
import com.tkpm.sms.application.dto.request.student.StudentUpdateRequestDto;
import com.tkpm.sms.application.mapper.AddressMapper;
import com.tkpm.sms.application.mapper.IdentityMapper;
import com.tkpm.sms.application.service.interfaces.StudentService;
import com.tkpm.sms.application.service.implementation.StudentServiceImpl;
import com.tkpm.sms.application.service.interfaces.*;
import com.tkpm.sms.domain.enums.IdentityType;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.Faculty;
import com.tkpm.sms.domain.model.Identity;
import com.tkpm.sms.domain.model.Program;
import com.tkpm.sms.domain.model.Status;
import com.tkpm.sms.domain.model.Student;
import com.tkpm.sms.domain.repository.StudentRepository;
import com.tkpm.sms.domain.service.validators.IdentityDomainValidator;
import com.tkpm.sms.domain.service.validators.StudentDomainValidator;
import com.tkpm.sms.domain.valueobject.Phone;
import com.tkpm.sms.infrastructure.mapper.StudentMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("StudentService Tests")
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private IdentityDomainValidator identityValidator;

    @Mock
    private StudentDomainValidator studentValidator;

    @Mock
    private StatusService statusService;

    @Mock
    private ProgramService programService;

    @Mock
    private FacultyService facultyService;

    @Mock
    private IdentityService identityService;

    @Mock
    private StudentMapperImpl studentMapper;

    @Mock
    private AddressMapper addressMapper;

    @Mock
    private IdentityMapper identityMapper;

    @Mock
    private PhoneParser phoneParser;

    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        studentService = new StudentServiceImpl(studentRepository, identityValidator,
                studentValidator, statusService, programService, facultyService, identityService,
                studentMapper, addressMapper, identityMapper, phoneParser);
    }

    @Nested
    @DisplayName("Find Students Tests")
    class FindStudentsTests {
        // @Test
        // @DisplayName("Should find all students with filters")
        // void testFindAll() {
        // StudentCollectionRequest request = new StudentCollectionRequest();
        // request.setSearch("test");
        // request.setFaculty("faculty");
        // PageResponse<Student> expectedResponse = new
        // PageResponse<Student>(Collections.emptyList(), 1, 10, 0, 1);

        // when(studentRepository.findWithFilters(anyString(), anyString(),
        // any(PageRequest.class)))
        // .thenReturn(expectedResponse);

        // PageResponse<Student> result = studentService.findAll(request);

        // assertNotNull(result);
        // verify(studentRepository).findWithFilters(eq("test"), eq("faculty"),
        // any(PageRequest.class));
        // }

        @Test
        @DisplayName("Should get student details successfully")
        void testGetStudentDetail() {
            String studentId = "1";
            Student student = new Student();
            when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

            Student result = studentService.getStudentDetail(studentId);

            assertNotNull(result);
            assertEquals(student, result);
        }

        @Test
        @DisplayName("Should throw exception when student not found")
        void testGetStudentDetail_NotFound() {
            String studentId = "1";
            when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> studentService.getStudentDetail(studentId));
        }
    }

    @Nested
    @DisplayName("Create Student Tests")
    class CreateStudentTests {
        @Test
        @DisplayName("Should create student successfully")
        void testCreateStudent() {
            StudentCreateRequestDto requestDto = createStudentCreateRequestDto();
            Student student = new Student();
            Phone phone = Phone.of("+1", "1234567890");
            Faculty faculty = new Faculty();
            Program program = new Program();
            Status status = new Status();

            setupCreateStudentMocks(requestDto, student, phone, faculty, program, status);

            Student result = studentService.createStudent(requestDto);

            assertNotNull(result);
            verifyCreateStudentMocks(requestDto);
        }

        private StudentCreateRequestDto createStudentCreateRequestDto() {
            StudentCreateRequestDto dto = new StudentCreateRequestDto();
            dto.setStudentId("ST001");
            dto.setEmail("test@example.com");

            PhoneRequestDto phoneDto = new PhoneRequestDto();
            phoneDto.setCountryCode("+1");
            phoneDto.setPhoneNumber("1234567890");
            dto.setPhone(phoneDto);

            dto.setFacultyId(1);
            dto.setProgramId(1);
            dto.setStatusId(1);

            return dto;
        }

        private void setupCreateStudentMocks(StudentCreateRequestDto requestDto, Student student,
                Phone phone, Faculty faculty, Program program, Status status) {
            when(phoneParser.parsePhoneNumberToPhone(anyString(), anyString())).thenReturn(phone);
            when(studentMapper.toStudent(requestDto)).thenReturn(student);
            when(facultyService.getFacultyByName(anyString())).thenReturn(faculty);
            when(programService.getProgramByName(anyString())).thenReturn(program);
            when(statusService.getStatusByName(anyString())).thenReturn(status);
            when(studentRepository.save(any(Student.class))).thenReturn(student);
        }

        private void verifyCreateStudentMocks(StudentCreateRequestDto requestDto) {
            verify(studentValidator).validateStudentIdUniqueness(requestDto.getStudentId());
            verify(studentValidator).validateEmailUniqueness(requestDto.getEmail());
            verify(studentValidator).validateEmailDomain(requestDto.getEmail());
            verify(studentRepository).save(any(Student.class));
        }
    }

    @Nested
    @DisplayName("Update Student Tests")
    class UpdateStudentTests {
        @Test
        @DisplayName("Should update student successfully")
        void testUpdateStudent() {
            String id = "1";
            StudentUpdateRequestDto requestDto = createStudentUpdateRequestDto();
            Student student = createExistingStudent();
            Phone phone = Phone.of("+1", "1234567890");
            Faculty faculty = new Faculty();
            Program program = new Program();
            Status status = new Status();

            setupUpdateStudentMocks(id, requestDto, student, phone, faculty, program, status);

            Student result = studentService.updateStudent(id, requestDto);

            assertNotNull(result);
            verifyUpdateStudentMocks(id, requestDto, student);
        }

        private StudentUpdateRequestDto createStudentUpdateRequestDto() {
            StudentUpdateRequestDto dto = new StudentUpdateRequestDto();
            dto.setStudentId("ST001");
            dto.setEmail("test@example.com");

            PhoneRequestDto phoneDto = new PhoneRequestDto();
            phoneDto.setCountryCode("+1");
            phoneDto.setPhoneNumber("1234567890");
            dto.setPhone(phoneDto);

            dto.setFacultyId(1);
            dto.setProgramId(1);
            dto.setStatusId(1);

            IdentityUpdateRequestDto identityDto = new IdentityUpdateRequestDto();
            identityDto.setType("Passport");
            identityDto.setNumber("ABC123");
            dto.setIdentity(identityDto);

            return dto;
        }

        private Student createExistingStudent() {
            Student student = new Student();
            student.setStudentId("ST001");
            student.setEmail("old@example.com");

            Identity identity = new Identity();
            identity.setType(IdentityType.PASSPORT);
            identity.setNumber("OLD123");
            student.setIdentity(identity);

            Status status = new Status();
            status.setId(1);
            student.setStatus(status);

            return student;
        }

        private void setupUpdateStudentMocks(String id, StudentUpdateRequestDto requestDto,
                Student student, Phone phone, Faculty faculty,
                Program program, Status status) {
            when(studentRepository.findById(id)).thenReturn(Optional.of(student));
            when(phoneParser.parsePhoneNumberToPhone(anyString(), anyString())).thenReturn(phone);
            when(statusService.getStatusById(any())).thenReturn(status);
            when(programService.getProgramById(any())).thenReturn(program);
            when(facultyService.getFacultyById(any())).thenReturn(faculty);
            when(studentRepository.save(any(Student.class))).thenReturn(student);
        }

        private void verifyUpdateStudentMocks(String id, StudentUpdateRequestDto requestDto,
                Student student) {
            verify(studentRepository).findById(id);
            verify(studentValidator).validateEmailDomain(requestDto.getEmail());
            verify(studentValidator).validateStatusTransition(eq(student), any(Status.class));
            verify(studentRepository).save(student);
        }

        @Test
        @DisplayName("Should throw exception when student not found during update")
        void testUpdateStudent_NotFound() {
            String id = "1";
            StudentUpdateRequestDto requestDto = new StudentUpdateRequestDto();
            when(studentRepository.findById(id)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> studentService.updateStudent(id, requestDto));
        }
    }

    @Nested
    @DisplayName("Delete Student Tests")
    class DeleteStudentTests {
        @Test
        @DisplayName("Should delete student successfully")
        void testDeleteStudent() {
            String id = "1";
            Student student = new Student();
            when(studentRepository.findById(id)).thenReturn(Optional.of(student));
            doNothing().when(studentRepository).delete(student);

            studentService.deleteStudentById(id);

            verify(studentRepository).findById(id);
            verify(studentRepository).delete(student);
        }

        @Test
        @DisplayName("Should throw exception when student not found during deletion")
        void testDeleteStudent_NotFound() {
            String id = "1";
            when(studentRepository.findById(id)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> studentService.deleteStudentById(id));
        }
    }

    // @Nested
    // @DisplayName("Import Students Tests")
    // class ImportStudentsTests {
    // @Test
    // @DisplayName("Should import students from file successfully")
    // void testSaveListStudentFromFile() {
    // List<StudentFileDto> fileDtos = Collections.singletonList(new
    // StudentFileDto());
    // StudentCreateRequestDto createRequestDto = new StudentCreateRequestDto();
    // Student student = new Student();
    // Phone phone = Phone.of("+1", "1234567890");
    // Faculty faculty = new Faculty();
    // Program program = new Program();
    // Status status = new Status();

    // when(studentMapper.toStudentCreateRequest(any(StudentFileDto.class)))
    // .thenReturn(createRequestDto);
    // when(studentMapper.toStudent(createRequestDto)).thenReturn(student);
    // when(phoneParser.parsePhoneNumberToPhone(anyString(),
    // anyString())).thenReturn(phone);
    // when(facultyService.getFacultyByName(anyString())).thenReturn(faculty);
    // when(programService.getProgramByName(anyString())).thenReturn(program);
    // when(statusService.getStatusByName(anyString())).thenReturn(status);
    // when(studentRepository.save(any(Student.class))).thenReturn(student);

    // studentService.saveListStudentFromFile(fileDtos);

    // verify(studentMapper).toStudentCreateRequest(any(StudentFileDto.class));
    // verify(studentRepository).save(any(Student.class));
    // }
    // }
}