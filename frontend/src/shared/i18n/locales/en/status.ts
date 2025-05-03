export default {
  title: 'Status Management',
  addNew: 'Add Status',
  editStatus: 'Edit Status',
  statusDetails: 'Status Details',
  statusNotFound: 'Status not found',
  fields: {
    id: 'ID',
    name: 'Name',
    namePlaceholder: 'Enter status name',
    allowedTransitions: 'Allowed Transitions',
    searchPlaceholder: 'Search status...',
  },
  sections: {
    statusInfo: 'Status Information',
    transitions: 'Allowed Transitions',
  },
  validation: {
    required: 'This field is required',
    maxLength: 'Name must be less than {{length}} characters',
    nameFormat: 'Name must contain only letters and spaces',
  },
  messages: {
    statusAdded: 'Status added successfully',
    statusUpdated: 'Status updated successfully',
    statusDeleted: 'Status deleted successfully',
    confirmDelete: 'Are you sure you want to delete this status?',
    noTransitions: 'No transitions selected',
    selectStatus: 'Select status',
  },
  actions: {
    remove: 'Remove {{name}}',
    activate: 'Activate',
    deactivate: 'Deactivate',
  },
  noAllowedTransitions: 'No allowed transitions',
  defaultStatuses: {
    studying: 'Studying',
    graduated: 'Graduated',
    suspended: 'Suspended',
    expelled: 'Expelled',
    onLeave: 'On Leave',
  },
};
