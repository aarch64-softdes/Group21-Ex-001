export default {
  title: 'Faculty Management',
  addNew: 'Add Faculty',
  editFaculty: 'Edit Faculty',
  facultyDetails: 'Faculty Details',
  facultyNotFound: 'Faculty not found',
  fields: {
    id: 'ID',
    name: 'Name',
    faculty: 'Faculty',
    namePlaceholder: 'Enter faculty name',
  },
  sections: {
    facultyInfo: 'Faculty Information',
  },
  validation: {
    required: 'This field is required',
    maxLength: 'Name must be less than {{length}} characters',
    nameFormat: 'Name must contain only letters and spaces',
  },
  messages: {
    facultyAdded: 'Faculty added successfully',
    facultyUpdated: 'Faculty updated successfully',
    facultyDeleted: 'Faculty deleted successfully',
    confirmDelete: 'Are you sure you want to delete this faculty?',
  },
};
