export default {
  title: 'Quản lý Trạng thái',
  addNew: 'Thêm Trạng thái Mới',
  editStatus: 'Sửa Thông tin Trạng thái',
  statusDetails: 'Chi tiết Trạng thái',
  statusNotFound: 'Không tìm thấy Trạng thái',
  fields: {
    id: 'ID',
    name: 'Tên Trạng thái',
    namePlaceholder: 'Nhập tên Trạng thái',
    allowedTransitions: 'Chuyển đổi Trạng thái Cho phép',
    searchPlaceholder: 'Tìm kiếm trạng thái...',
  },
  sections: {
    statusInfo: 'Thông tin Trạng thái',
    transitions: 'Chuyển đổi Trạng thái Cho phép',
  },
  validation: {
    required: 'Trường này là bắt buộc',
    maxLength: 'Tên phải ít hơn {{length}} ký tự',
    nameFormat: 'Tên chỉ được chứa chữ cái và khoảng trắng',
  },
  messages: {
    statusAdded: 'Thêm Trạng thái thành công',
    statusUpdated: 'Cập nhật Trạng thái thành công',
    statusDeleted: 'Xóa Trạng thái thành công',
    confirmDelete: 'Bạn có chắc chắn muốn xóa Trạng thái này?',
    noTransitions: 'Không có trạng thái chuyển đổi được chọn',
    selectStatus: 'Chọn trạng thái',
  },
  actions: {
    remove: 'Xóa {{name}}',
    activate: 'Kích hoạt',
    deactivate: 'Vô hiệu hóa',
  },
  noAllowedTransitions: 'Không có trạng thái chuyển đổi được phép',
  defaultStatuses: {
    studying: 'Đang học',
    graduated: 'Đã tốt nghiệp',
    suspended: 'Đình chỉ',
    expelled: 'Buộc thôi học',
    onLeave: 'Tạm nghỉ',
  },
};
