export default {
  title: 'Settings',
  emailDomain: {
    title: 'Email Domain Setting',
    description: 'Configure the allowed email domain for all students',
    label: 'Email Domain',
    placeholder: '@example.com',
    help: 'This domain will be enforced for all student email addresses.',
    confirmTitle: 'Confirm Email Domain Change',
    confirmDescription:
      'You are about to change the allowed email domain to {domain}. This will affect validation for all student email numbers. Are you sure?',
  },
  phoneNumber: {
    title: 'Phone Number Settings',
    description: 'Configure allowed country codes for phone numbers',
    label: 'Supported Country Codes',
    noCountry: 'No country codes selected',
    help: 'These country codes will be allowed for student phone numbers.',
    confirmTitle: 'Confirm Country Code Changes',
    confirmDescription:
      'You are about to update the allowed country codes for phone numbers. This will affect validation for all student phone numbers. Are you sure?',
    selectedCountries: 'Selected Countries:',
  },
  adjustmentDuration: {
    title: 'Enrollment Adjustment Period',
    description:
      'Configure the number of days students can adjust their course enrollments after the start date',
    label: 'Adjustment Period (days)',
    help: 'Students will be able to enroll or unenroll from courses during this period after the course start date.',
    confirmTitle: 'Confirm Adjustment Period Change',
    confirmDescription:
      'You are about to change the enrollment adjustment period to {duration} days. This will affect all course enrollment periods. Are you sure?',
  },
  actions: {
    edit: 'Edit',
    save: 'Save',
    cancel: 'Cancel',
    confirm: 'Confirm',
  },
  messages: {
    updated: '{setting} updated successfully',
    updateFailed: 'Failed to update {setting}: {error}',
  },
};
