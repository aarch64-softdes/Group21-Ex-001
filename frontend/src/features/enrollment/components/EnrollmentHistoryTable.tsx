import React from 'react';
import { Badge } from '@ui/badge';
import { Column, QueryHookParams } from '@/core/types/table';
import GenericTable from '@/components/table/GenericTable';
import EnrollmentService from '../api/enrollmentService';
import { useQuery } from '@tanstack/react-query';
import { EnrollmentHistory } from '../types/enrollment';

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

  const enrollmentService = new EnrollmentService();
  const useEnrollmentHistory = () => {
    const useEnrollments = (query: QueryHookParams) => {
      let { page, pageSize } = query;

      if (page < 1) {
        page = 1;
      }

      return useQuery({
        queryKey: ['enrollmentHistory', studentId, page, pageSize],
        queryFn: () =>
          enrollmentService.getEnrollmentHistory(studentId, page, pageSize),
      });
    };

    return useEnrollments;
  };

  return (
    <div className='bg-white rounded-md p-4'>
      <GenericTable
        columns={columns}
        addAction={{ disabled: true, onAdd: () => {} }}
        queryHook={useEnrollmentHistory()}
        filterOptions={[]}
        metadata={studentId}
        emptyMessage='No enrollment history found.'
      />
    </div>
  );
};

export default EnrollmentHistoryTable;
