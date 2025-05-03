export default {
  title: 'Quản lý Trạng thái',
  addNew: 'Thêm Trạng thái Mới',
  editStatus: 'Sửa Thông tin Trạng thái',
  statusDetails: 'Chi tiết Trạng thái',
  fields: {
    id: 'ID',
    name: 'Tên Trạng thái',
    allowedTransitions: 'Chuyển đổi Trạng thái Cho phép',
  },
  sections: {
    statusInfo: 'Thông tin Trạng thái',
    transitions: 'Chuyển đổi Trạng thái Cho phép',
  },
  messages: {
    statusAdded: 'Thêm Trạng thái thành công',
    statusUpdated: 'Cập nhật Trạng thái thành công',
    statusDeleted: 'Xóa Trạng thái thành công',
    confirmDelete: 'Bạn có chắc chắn muốn xóa Trạng thái này?',
    noTransitions: 'Không có trạng thái chuyển đổi được chọn',
    selectStatus: 'Chọn trạng thái',
  },
  defaultStatuses: {
    studying: 'Đang học',
    graduated: 'Đã tốt nghiệp',
    suspended: 'Đình chỉ',
    expelled: 'Buộc thôi học',
    onLeave: 'Tạm nghỉ',
  },
};
