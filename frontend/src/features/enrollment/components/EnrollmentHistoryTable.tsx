import React from 'react';
import { Badge } from '@ui/badge';
import { Column } from '@/core/types/table';
import GenericTable from '@/components/table/GenericTable';
import { EnrollmentHistory } from '../types/enrollment';
import { useEnrollmentHistory } from '../api/useEnrollmentApi';

interface EnrollmentHistoryTableProps {
  studentId: string;
}

const EnrollmentHistoryTable: React.FC<EnrollmentHistoryTableProps> = ({
  studentId,
}) => {
  // Action badge component for display
  const ActionBadge = ({ actionType }: { actionType: string }) => {
    switch (actionType.toLowerCase()) {
      case 'enrolled':
        return <Badge variant='default'>Enrolled</Badge>;
      case 'deleted':
        return <Badge variant='destructive'>Unenrolled</Badge>;
      default:
        return <Badge variant='secondary'>{actionType}</Badge>;
    }
  };

  // Column definitions
  const columns: Column<EnrollmentHistory>[] = [
    {
      header: 'Date',
      key: 'createdAt',
      transform: (value) =>
        new Date(value).toLocaleString('en-US', {
          year: 'numeric',
          month: 'short',
          day: 'numeric',
          hour: '2-digit',
          minute: '2-digit',
        }),
    },
    {
      header: 'Action',
      key: 'actionType',
      transform: (value) => <ActionBadge actionType={value} />,
    },
    { header: 'Course Code', key: 'course.code', nested: true },
    { header: 'Course Name', key: 'course.subject.name', nested: true },
    {
      header: 'Semester',
      key: 'course.semester',
      nested: true,
      transform: (value, row) => `${row?.course?.year}, Semester ${value}`,
    },
  ];

  return (
    <div className='bg-white rounded-md p-4'>
      <GenericTable
        columns={columns}
        addAction={{ disabled: true, onAdd: () => {} }}
        queryHook={useEnrollmentHistory(studentId)}
        disabledActionCell={true}
        filterOptions={[]}
        metadata={studentId}
        emptyMessage='No enrollment history found.'
      />
    </div>
  );
};

export default EnrollmentHistoryTable;
