export default {
  title: 'Quản lý Khoa',
  addNew: 'Thêm Khoa Mới',
  editFaculty: 'Sửa Thông tin Khoa',
  facultyDetails: 'Chi tiết Khoa',
  facultyNotFound: 'Không tìm thấy Khoa',
  fields: {
    id: 'ID',
    name: 'Tên Khoa',
    faculty: 'Khoa',
    namePlaceholder: 'Nhập tên Khoa',
  },
  sections: {
    facultyInfo: 'Thông tin Khoa',
  },
  validation: {
    required: 'Trường này là bắt buộc',
    maxLength: 'Tên phải ít hơn {{length}} ký tự',
    nameFormat: 'Tên chỉ được chứa chữ cái và khoảng trắng',
  },
  messages: {
    facultyAdded: 'Thêm Khoa thành công',
    facultyUpdated: 'Cập nhật Khoa thành công',
    facultyDeleted: 'Xóa Khoa thành công',
    confirmDelete: 'Bạn có chắc chắn muốn xóa Khoa này?',
  },
};
