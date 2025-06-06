export default {
  title: 'Quản lý Đăng ký Khóa học',
  return: 'Quay lại',
  student: 'Sinh viên',
  tabs: {
    available: 'Khóa học Khả dụng',
    current: 'Khóa học Hiện tại',
    transcript: 'Bảng điểm',
    history: 'Lịch sử Đăng ký',
  },
  availableCourses: {
    code: 'Mã khóa học',
    subject: 'Môn học',
    schedule: 'Lịch học',
    room: 'Phòng học',
    semester: 'Học kỳ',
    actions: 'Thao tác',
    enroll: 'Đăng ký',
    noCoursesFound: 'Không tìm thấy khóa học khả dụng.',
    confirmEnrollment: 'Xác nhận Đăng ký',
    confirmEnrollmentMessage: 'Bạn có chắc chắn muốn đăng ký khóa học này?',
    enrolling: 'Đang đăng ký...',
  },
  currentEnrollments: {
    code: 'Mã khóa học',
    subject: 'Môn học',
    schedule: 'Lịch học',
    room: 'Phòng học',
    semester: 'Học kỳ',
    grade: 'Điểm',
    actions: 'Thao tác',
    editGrade: 'Điểm',
    unenroll: 'Hủy đăng ký',
    noEnrollments: 'Bạn chưa đăng ký khóa học nào.',
    confirmUnenrollment: 'Xác nhận Hủy đăng ký',
    confirmUnenrollmentMessage:
      'Bạn có chắc chắn muốn hủy đăng ký khóa học này? Thao tác này không thể hoàn tác.',
    unenrolling: 'Đang hủy đăng ký...',
    gradeStatus: {
      pending: 'Chưa có điểm',
      notAvailable: 'Không khả dụng',
    },
    updateGrade: 'Cập nhật Điểm',
    updateGradeMessage: 'Nhập điểm và GPA cho đăng ký khóa học này.',
    gradePlaceholder: 'A, B+, C, v.v.',
    gpaPlaceholder: '0.00-4.00',
    saving: 'Đang lưu...',
  },
  history: {
    date: 'Ngày',
    action: 'Thao tác',
    courseCode: 'Mã khóa học',
    courseName: 'Tên môn học',
    semester: 'Học kỳ',
    noHistory: 'Không tìm thấy lịch sử đăng ký.',
    actionTypes: {
      enrolled: 'Đã đăng ký',
      unenrolled: 'Đã hủy đăng ký',
    },
  },
  transcript: {
    title: 'Bảng điểm Học tập',
    subtitle: 'Bảng điểm chính thức của sinh viên',
    studentInfo: 'Thông tin Sinh viên',
    name: 'Họ tên',
    id: 'Mã số',
    dob: 'Ngày sinh',
    program: 'Chương trình',
    academicSummary: 'Tổng kết Học tập',
    cumulativeGPA: 'Điểm trung bình tích lũy',
    totalCredits: 'Tổng số tín chỉ',
    coursesCompleted: 'Số môn học đã hoàn thành',
    courseDetails: 'Chi tiết Khóa học',
    subjectCode: 'Mã môn học',
    subjectName: 'Tên môn học',
    grade: 'Điểm chữ',
    gpa: 'Điểm số',
    generatedOn: 'Ngày tạo',
    system: 'Hệ thống Quản lý Sinh viên',
    print: 'In',
    download: 'Tải xuống',
    exporting: 'Đang xuất...',
    noTranscriptData: 'Không có dữ liệu bảng điểm.',
  },
  availableStudents: {
    searchPlaceholder: 'Tìm kiếm sinh viên theo tên hoặc mã số...',
    studentId: 'Mã sinh viên',
    name: 'Họ tên',
    faculty: 'Khoa',
    program: 'Chương trình',
    status: 'Trạng thái',
    actions: 'Thao tác',
    enroll: 'Đăng ký',
    noStudentsFound: 'Không tìm thấy sinh viên nào.',
    confirmEnrollment: 'Xác nhận Đăng ký Sinh viên',
    confirmEnrollmentMessage:
      'Bạn có chắc chắn muốn đăng ký sinh viên này vào khóa học?',
    enrolling: 'Đang đăng ký...',
  },
  messages: {
    enrollSuccess: 'Đăng ký khóa học thành công',
    unenrollSuccess: 'Hủy đăng ký khóa học thành công',
    updateTranscriptSuccess: 'Cập nhật bảng điểm thành công',
    operationFailed: '{operation} thất bại: {error}',
  },
};
