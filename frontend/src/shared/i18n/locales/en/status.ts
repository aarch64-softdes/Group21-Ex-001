export default {
  title: 'Status Management',
  addNew: 'Add Status',
  editStatus: 'Edit Status',
  statusDetails: 'Status Details',
  fields: {
    id: 'ID',
    name: 'Name',
    allowedTransitions: 'Allowed Transitions',
  },
  sections: {
    statusInfo: 'Status Information',
    transitions: 'Allowed Transitions',
  },
  messages: {
    statusAdded: 'Status added successfully',
    statusUpdated: 'Status updated successfully',
    statusDeleted: 'Status deleted successfully',
    confirmDelete: 'Are you sure you want to delete this status?',
    noTransitions: 'No transitions selected',
    selectStatus: 'Select status',
  },
  defaultStatuses: {
    studying: 'Studying',
    graduated: 'Graduated',
    suspended: 'Suspended',
    expelled: 'Expelled',
    onLeave: 'On Leave',
  },
};
