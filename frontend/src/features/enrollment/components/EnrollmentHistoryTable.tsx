import React from 'react';
import { Badge } from '@ui/badge';
import { Column } from '@/core/types/table';
import GenericTable from '@/components/table/GenericTable';
import { EnrollmentHistory } from '../types/enrollment';
import { useEnrollmentHistory } from '../api/useEnrollmentApi';
import { useTranslation } from 'react-i18next';

interface EnrollmentHistoryTableProps {
  studentId: string;
}

const EnrollmentHistoryTable: React.FC<EnrollmentHistoryTableProps> = ({
  studentId,
}) => {
  const { t } = useTranslation('enrollment');

  // Action badge component for display
  const ActionBadge = ({ actionType }: { actionType: string }) => {
    switch (actionType.toLowerCase()) {
      case 'enrolled':
        return (
          <Badge variant='default'>{t('history.actionTypes.enrolled')}</Badge>
        );
      case 'deleted':
        return (
          <Badge variant='destructive'>
            {t('history.actionTypes.unenrolled')}
          </Badge>
        );
      default:
        return <Badge variant='secondary'>{actionType}</Badge>;
    }
  };

  // Column definitions
  const columns: Column<EnrollmentHistory>[] = [
    {
      header: t('history.date'),
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
      header: t('history.action'),
      key: 'actionType',
      transform: (value) => <ActionBadge actionType={value} />,
    },
    { header: t('history.courseCode'), key: 'course.code', nested: true },
    {
      header: t('history.courseName'),
      key: 'course.subject.name',
      nested: true,
    },
    {
      header: t('history.semester'),
      key: 'course.semester',
      nested: true,
      transform: (value, row) =>
        `${row?.course?.year}, ${t('history.semester')} ${value}`,
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
        emptyMessage={t('history.noHistory')}
      />
    </div>
  );
};

export default EnrollmentHistoryTable;
