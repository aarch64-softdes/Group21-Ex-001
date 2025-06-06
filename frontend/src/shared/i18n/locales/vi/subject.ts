export default {
  title: 'Quản lý Môn học',
  addNew: 'Thêm Môn học Mới',
  editSubject: 'Sửa Thông tin Môn học',
  subjectDetails: 'Chi tiết Môn học',
  fields: {
    id: 'ID',
    name: 'Tên Môn học',
    namePlaceholder: 'Nhập tên Môn học',
    code: 'Mã Môn học',
    credits: 'Số tín chỉ',
    description: 'Mô tả',
    descriptionPlaceholder: 'Nhập mô tả môn học',
    faculty: 'Khoa',
    selectFaculty: 'Chọn khoa',
    searchFaculty: 'Tìm kiếm khoa',
    prerequisites: 'Môn học tiên quyết',
    selectPrerequisites: 'Thêm môn học tiên quyết',
    searchPrerequisites: 'Tìm kiếm môn học tiên quyết',
    isActive: 'Trạng thái',
    status: {
      active: 'Hoạt động',
      inactive: 'Không hoạt động',
    },
  },
  sections: {
    subjectInfo: 'Thông tin Môn học',
    description: 'Mô tả',
    prerequisites: 'Môn học tiên quyết',
  },
  messages: {
    subjectAdded: 'Thêm Môn học thành công',
    subjectUpdated: 'Cập nhật Môn học thành công',
    subjectDeleted: 'Xóa Môn học thành công',
    subjectActivated: 'Kích hoạt Môn học thành công',
    subjectDeactivated: 'Vô hiệu hóa Môn học thành công',
    confirmDelete: 'Bạn có chắc chắn muốn xóa Môn học này?',
    confirmActivate: 'Bạn có chắc chắn muốn kích hoạt Môn học này?',
    confirmDeactivate: 'Bạn có chắc chắn muốn vô hiệu hóa Môn học này?',
    noPrerequisites: 'Không có môn học tiên quyết được chọn',
    noPrerequisitesSelected: 'Không có môn học tiên quyết được chọn',
    addPrerequisite: 'Thêm môn học tiên quyết',
  },
  actions: {
    activate: 'Kích hoạt',
    deactivate: 'Vô hiệu hóa',
    create: 'Thêm Môn học',
    update: 'Lưu Thay đổi',
  },
  validation: {
    required: 'Trường này là bắt buộc',
    nameRequired: 'Tên Môn học là bắt buộc',
    codeRequired: 'Mã Môn học là bắt buộc',
    nameTooLong: 'Tên Môn học phải ngắn hơn 255 ký tự',
    codeTooLong: 'Mã Môn học phải ngắn hơn 20 ký tự',
    creditsInvalid: 'Số tín chỉ phải là một số',
    creditsMin: 'Số tín chỉ phải ít nhất là 2',
    creditsMax: 'Số tín chỉ không được vượt quá 10',
    descriptionTooLong: 'Mô tả phải ngắn hơn 1000 ký tự',
    facultyRequired: 'Khoa là bắt buộc',
  },
};
