# Yêu Cầu và Phương Pháp Tốt Nhất về Kiểm Thử Đơn Vị cho Ứng dụng Quản lý Sinh viên

## 1. Unit test
        
- Unit test, tạm dịch là “kiểm thử đơn vị”, là một phương pháp kiểm thử phần mềm tập trung vào việc kiểm tra các thành phần nhỏ nhất của ứng dụng, thường là các function hoặc method nhằm đảm bảo rằng chúng hoạt động đúng như mong đợi trong môi trường cô lập. 
- Điều này giúp phát hiện lỗi trong quá trình phát triển, tăng chất lượng phần mềm và giảm chi phí bảo trì.

## 2. Code Coverage

- Code coverage là một đại lượng được sử dụng để đo độ bao phủ của các code được thực thi trên toàn bộ source code của ứng dụng.
- Code coverage giúp developer xác định được những phần đã được test hay chưa được test trong code, giúp đảm bảo chất lượng và độ tin cậy của phần mềm được kiểm thử. 
- Phân loại: Gồm các loại chính như sau:
    - Line coverage: Đo số lượng dòng trong source code đã được test.
    - Branch coverage: Đo số lượng nhánh trong các lệnh điều khiển như if, else, switch…
    - Function coverage: Đo số lượng function/method được gọi trong khi kiểm thử.
    - Path coverage: Đo số lượng luồng xử lí trong quá trình test. Luồng xử lí ở đây có thể hiểu là một chuỗi các bước trong quá trình thực thi. Tại mỗi bước, có thể xảy ra các trường hợp rẽ nhánh hoặc gọi hàm,… Vì vậy, path coverage bao gồm luôn cả branch coverage, function coverage hay các loại khác.

## 3. Tiêu chuẩn của industry

Theo trang https://www.bullseye.com/minimum.html, sau đây là code coverage thường sử dụng:

| Coverage Type | Minimum Acceptable | Good Practice | Excellent |
|---------------|-------------------|--------------|-----------|
| Line Coverage | 70-75% | 80-85% | >90% |
| Branch Coverage | 65-70% | 75-80% | >85% |
| Function Coverage | 80-85% | 90-95% | 100% |

**Các điểm cần chú ý:**
- Các hệ thống ở các doanh nghiệp thường duy trì khoảng 80% line coverage
- Các module quan trọng (xác thực, xác thực dữ liệu, ...) thường có coverage cao hơn (>85%)
- Hiếm đơn vị yêu cầu coverage 100% vì việc đó gây nhiều khó khăn trong quá trình phát triển ứng dụng, trong khi hiệu quả đem lại có thể không đáng kể.

**Tỉ lệ có thể được nhóm sử dụng:**
- Module chính: Line coverage và Function coverage đạt trên 85%.
- Utils: Line coverage trên 80%.
- Toàn bộ ứng dụng: Trên 80% line coverage.

## 4. Best practices cho Unit test

### 4.1 Cô lập với các dependency bên ngoài

- Thực hiện cô lập với database và các service khác bằng cách sử dụng:
    - Mock object để mô phỏng hoạt động cơ sở dữ liệu và các service.
    - Sử dụng interface để dễ dàng thay dổi các mock object.

**Ví dụ:**
```java
// Thay vì:
public void testStudentRegistration() {
    // Sử dụng trực tiếp DB thật
    studentRepository.save(newStudent);
    // ...
}

// Sử dụng:
public void testStudentRegistration() {
    // Mock repository
    StudentRepository mockRepo = mock(StudentRepository.class);
    when(mockRepo.save(any(Student.class))).thenReturn(newStudentWithId);
    
    StudentService service = new StudentService(mockRepo);
    Student result = service.registerStudent(newStudent);
    
    verify(mockRepo).save(newStudent);
    assertEquals(expectedId, result.getId());
}
```

### 4.2 Kịch bản test toàn bộ hệ thống

- Happy case:
    - Verify các behavior mong muốn với đầu vào hợp lệ.
    - Test xem các method có chạy thành công hay không.
    - Đảm bảo trạng thái của hệ thống và các giá trị trả về là chính xác.

- Edge case:
    - Input `null` hoặc rỗng.
    - Boundary values (thường là các giá trị giới hạn min/max của kiểu dữ liệu)
    - Dữ liệu sai format hoặc bị duplicate.

- Exception handling:
    - Test xem exception có được tạo và xử lí chính xác hay không.
    - Thông báo lỗi phù hợp.
    - Khôi phục hệ thống sau lỗi.

### 4.3. Cách tổ chức các testcases

- Cấu trúc test sử dụng mẫu Arrange-Act-Assert: Cung cấp cho các testcase trạng thái bắt đầu (arrange), cho code cần được kiểm thử thực thi với dữ liệu đã arrange (act), sau đó assert (có thể tạm hiểu là so sánh) dữ liệu có được từ code và dữ liệu mà ta cần nhận được.
- Nhóm các test theo tính năng.
- Tên test nên thể hiện rõ ràng các thông tin: Component được kiểm thử, Tên method được kiểm thử và mô tả ngắn gọn về expected behavior.
- Các test KHÔNG nên phụ thuộc lẫn nhau. Điều này giúp test hoạt động ổn định và chính xác hơn.

**Ví dụ:**
```java
@Test
public void registerStudent_WithValidInformation_ReturnsStudentWithId() {
    // Arrange
    Student newStudent = new Student("Jane", "Doe", "jane.doe@example.com");
    StudentRepository mockRepo = mock(StudentRepository.class);
    when(mockRepo.save(any(Student.class))).thenReturn(new Student(1L, "Jane", "Doe", "jane.doe@example.com"));
    StudentService service = new StudentService(mockRepo);
    
    // Act
    Student result = service.registerStudent(newStudent);
    
    // Assert
    assertNotNull(result.getId());
    assertEquals("Jane", result.getFirstName());
    assertEquals("Doe", result.getLastName());
    assertEquals("jane.doe@example.com", result.getEmail());
}
```

### 4.4. Các sai lầm thường gặp

**Không nên**
- Test quá chi tiết implementation thay vì behavior.
- Test phụ thuộc quá chặt vào implementation, khiến cho khi implementation thay đổi, test không hoạt động.
- Test chạy quá chậm, làm giảm tốc độ của các bên.
- Test dư thừa, không mang lại giá trị.

**Nên**
- Tập trung vào test các public interface và behavior có thể xác định được.
- Sử dụng parameterized test với các trường hợp cùng test case nhưng khác input.
- Nâng cao chất lượng test.
- Đảm bảo hiệu năng của test, không quá chậm.

## 5. Chiến lược cài đặt test của nhóm

1. Ưu tiên với các luồng chính, ví dụ như:
   - Thêm học sinh
   - Xác thực, phân quyền.
   - Đăng kí môn học.
   - Lưu trữ điểm và tính toán trên hệ thống.

2. Test các nghiệp vụ phức tạp của hệ thống, chú ý đến các thay đổi ảnh hưởng nghiêm trọng đến hệ thống.

3. Tăng dần coverage theo thời gian: Đảm bảo function coverage và sau mở rộng dần sang branch coverage và path coverage, ưu tiên từ các module quan trọng trước, sau mới đến các module khác.

### 5.2. Các công cụ kiểm thử

- Backend: JUnit/TestNG (Java)
- Frontend: Jest (TypeScript), manual test. 
- Mockito, Moq, hoặc các framework mocking tương tự
- JaCoCo, Istanbul, hoặc Coverage.py để báo cáo độ phủ

## 6. Kết Luận

- Việc triển khai unit test hiệu quả cần chú ý về sự cân bằng giữa coverage và quá trình kiểm thử thực tế. Bằng cách tập trung vào chức năng quan trọng, cô lập đúng các dependency và viết kịch bản kiểm thử hợp lý, kiểm thử có thể đem lại độ tin cậy tương đối cao mà không tốn quá nhiều công sức/tiền của.

- Bằng cách tuân theo các best practices, Ứng dụng quản lý sinh viên của nhóm sẽ có độ tin cậy cao hơn, bảo trì dễ dàng hơn và phát hiện lỗi sớm tránh gây technical debt về sau.

