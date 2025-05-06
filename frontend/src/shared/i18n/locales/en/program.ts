export default {
  title: 'Program Management',
  addNew: 'Add Program',
  editProgram: 'Edit Program',
  programDetails: 'Program Details',
  programNotFound: 'Program not found',
  fields: {
    id: 'ID',
    name: 'Name',
    namePlaceholder: 'Enter program name',
  },
  sections: {
    programInfo: 'Program Information',
  },
  validation: {
    required: 'This field is required',
    maxLength: 'Name must be less than {{length}} characters',
    nameFormat: 'Name must contain only letters and spaces',
  },
  messages: {
    programAdded: 'Program added successfully',
    programUpdated: 'Program updated successfully',
    programDeleted: 'Program deleted successfully',
    confirmDelete: 'Are you sure you want to delete this program?',
  },
};
