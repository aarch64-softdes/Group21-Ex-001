import React, { useState } from 'react';
import {
  useEnrollments,
  useUnenrollCourse,
  useUpdateTranscript,
} from '../api/useEnrollmentApi';
import { Button } from '@ui/button';
import {
  AlertTriangle,
  CheckCircle2,
  Clock,
  Edit2,
  Loader2,
  XCircle,
} from 'lucide-react';
import { Column, QueryHookParams } from '@/core/types/table';
import GenericTable from '@/components/table/GenericTable';
import EnrollmentService from '../api/enrollmentService';
import { useQuery } from '@tanstack/react-query';
import { EnrollmentMinimal } from '../types/enrollment';
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from '@ui/tooltip';

interface CurrentEnrollmentsTableProps {
  studentId: string;
  isAdmin?: boolean;
}

const GradeDisplay = ({ grade, gpa }: { grade?: string; gpa?: number }) => {
  if (!grade && !gpa) {
    return (
      <div className='flex items-center text-gray-400'>
        <Clock className='h-4 w-4 mr-1' />
        <span>Pending</span>
      </div>
    );
  }

  let color = 'text-gray-500';
  let icon = <Clock className='h-4 w-4 mr-1' />;

  if (gpa !== undefined) {
    if (gpa >= 3.5) {
      color = 'text-green-600';
      icon = <CheckCircle2 className='h-4 w-4 mr-1' />;
    } else if (gpa >= 2.0) {
      color = 'text-amber-500';
      icon = <AlertTriangle className='h-4 w-4 mr-1' />;
    } else {
      color = 'text-red-500';
      icon = <XCircle className='h-4 w-4 mr-1' />;
    }
  }

  return (
    <TooltipProvider>
      <Tooltip>
        <TooltipTrigger asChild>
          <div className={`flex items-center ${color}`}>
            {icon}
            <span className='font-medium'>{grade || 'N/A'}</span>
          </div>
        </TooltipTrigger>
        <TooltipContent>
          <p>Grade: {grade || 'Not available'}</p>
          <p>GPA: {gpa !== undefined ? gpa.toFixed(2) : 'Not available'}</p>
        </TooltipContent>
      </Tooltip>
    </TooltipProvider>
  );
};

const CurrentEnrollmentsTable: React.FC<CurrentEnrollmentsTableProps> = ({
  studentId,
  isAdmin = false,
}) => {
  const unenrollCourse = useUnenrollCourse();
  const updateTranscript = useUpdateTranscript();

  // Simple column definitions
  const columns: Column<EnrollmentMinimal>[] = [
    { header: 'Code', key: 'course.code', nested: true },
    { header: 'Subject', key: 'course.subject.name', nested: true },
    { header: 'Schedule', key: 'course.schedule', nested: true },
    { header: 'Room', key: 'course.room', nested: true },
    {
      header: 'Semester',
      key: 'course.semester',
      nested: true,
      transform: (value, row) => `${row?.course?.year}, Semester ${value}`,
    },
    {
      header: 'Grade',
      key: 'score',
      nested: true,
      transform: (value) => (
        <GradeDisplay grade={value?.grade} gpa={value?.gpa} />
      ),
    },
  ];

  // Simple action buttons
  const ActionButtons = ({ row }: { row: EnrollmentMinimal }) => {
    const handleUnenroll = () => {
      if (confirm('Are you sure you want to unenroll from this course?')) {
        unenrollCourse.mutate({
          studentId: studentId,
          courseId: row.course.id || '',
        });
      }
    };

    const handleUpdateGrade = () => {
      if (isAdmin) {
        const grade = prompt('Enter grade:', row.score?.grade || '');
        const gpa = prompt(
          'Enter GPA (0-4):',
          row.score?.gpa?.toString() || '',
        );

        if (grade !== null && gpa !== null) {
          updateTranscript.mutate({
            studentId: studentId,
            courseId: row.course.id || '',
            transcript: {
              grade: grade,
              gpa: parseFloat(gpa) || 0,
            },
          });
        }
      }
    };

    return (
      <div className='flex space-x-2'>
        {isAdmin && (
          <Button size='sm' variant='outline' onClick={handleUpdateGrade}>
            <Edit2 className='h-4 w-4 mr-1' />
            Grade
          </Button>
        )}
        <Button
          size='sm'
          variant='destructive'
          onClick={handleUnenroll}
          disabled={unenrollCourse.isPending}
        >
          {unenrollCourse.isPending ? 'Unenrolling...' : 'Unenroll'}
        </Button>
      </div>
    );
  };

  const enrollmentService = new EnrollmentService();
  const useEnrollment = () => {
    const useEnrollments = (query: QueryHookParams) => {
      let { page, pageSize } = query;

      if (page < 1) {
        page = 1;
      }

      return useQuery({
        queryKey: ['enrollments', studentId, page, pageSize],
        queryFn: () =>
          enrollmentService.getStudentEnrollments(studentId, page, pageSize),
      });
    };

    return useEnrollments;
  };

  return (
    <div className='bg-white rounded-md p-4'>
      <GenericTable
        columns={columns}
        addAction={{ disabled: true, onAdd: () => {} }}
        queryHook={useEnrollment()}
        filterOptions={[]}
        customActionCellComponent={ActionButtons}
        metadata={studentId}
        emptyMessage='You are not currently enrolled in any courses.'
      />
    </div>
  );
};

export default CurrentEnrollmentsTable;
