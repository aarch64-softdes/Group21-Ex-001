export default {
  title: 'Cài đặt',
  emailDomain: {
    title: 'Cài đặt Tên miền Email',
    description: 'Cấu hình tên miền email được phép cho tất cả sinh viên',
    label: 'Tên miền Email',
    placeholder: '@example.com',
    help: 'Tên miền này sẽ được áp dụng cho tất cả địa chỉ email của sinh viên.',
    confirmTitle: 'Xác nhận Thay đổi Tên miền Email',
    confirmDescription:
      'Bạn sắp thay đổi tên miền email được phép thành {domain}. Điều này sẽ ảnh hưởng đến quá trình xác thực cho tất cả email sinh viên. Bạn có chắc chắn không?',
    validation: {
      mustStartWithAt: 'Tên miền phải bắt đầu bằng @',
      invalidFormat: 'Định dạng tên miền không hợp lệ',
    },
  },
  phoneNumber: {
    title: 'Cài đặt Số điện thoại',
    description: 'Cấu hình mã quốc gia được phép cho số điện thoại',
    label: 'Mã Quốc gia Được hỗ trợ',
    noCountry: 'Không có mã quốc gia nào được chọn',
    help: 'Các mã quốc gia này sẽ được phép dùng cho số điện thoại của sinh viên.',
    confirmTitle: 'Xác nhận Thay đổi Mã Quốc gia',
    confirmDescription:
      'Bạn sắp cập nhật mã quốc gia được phép cho số điện thoại. Điều này sẽ ảnh hưởng đến quá trình xác thực cho tất cả số điện thoại sinh viên. Bạn có chắc chắn không?',
    selectedCountries: 'Quốc gia đã chọn:',
    selectCountry: 'Chọn quốc gia...',
    searchCountry: 'Tìm kiếm quốc gia...',
  },
  adjustmentDuration: {
    title: 'Thời gian Điều chỉnh Đăng ký',
    description:
      'Cấu hình số ngày sinh viên có thể điều chỉnh đăng ký khóa học sau ngày bắt đầu',
    label: 'Thời gian Điều chỉnh (ngày)',
    help: 'Sinh viên sẽ có thể đăng ký hoặc hủy đăng ký khóa học trong thời gian này sau ngày bắt đầu khóa học.',
    confirmTitle: 'Xác nhận Thay đổi Thời gian Điều chỉnh',
    confirmDescription:
      'Bạn sắp thay đổi thời gian điều chỉnh đăng ký thành {duration} ngày. Điều này sẽ ảnh hưởng đến tất cả thời gian đăng ký khóa học. Bạn có chắc chắn không?',
    validation: {
      required: 'Thời gian điều chỉnh là bắt buộc',
      mustBeNumber: 'Thời gian điều chỉnh phải là một số',
      minDuration: 'Thời gian điều chỉnh phải ít nhất 1 ngày',
      maxDuration: 'Thời gian điều chỉnh không được vượt quá 90 ngày',
    },
  },
  actions: {
    edit: 'Sửa',
    save: 'Lưu',
    cancel: 'Hủy',
    confirm: 'Xác nhận',
    saving: 'Đang lưu...',
  },
  messages: {
    updated: 'Cập nhật {setting} thành công',
    updateFailed: 'Không thể cập nhật {setting}: {error}',
  },
};
