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
import { Column } from '@/core/types/table';
import GenericTable from '@/components/table/GenericTable';
import { EnrollmentMinimal } from '../types/enrollment';
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from '@ui/tooltip';
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from '@ui/alert-dialog';

interface CurrentEnrollmentsTableProps {
  studentId: string;
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

interface CustomUnenrollButtonProps extends EnrollmentMinimal {
  studentId: string;
}

const UnenrollButton: React.FC<CustomUnenrollButtonProps> = ({
  id,
  course: { id: courseId },
  studentId,
}) => {
  const [isUnenrollDialogOpen, setIsUnenrollDialogOpen] = useState(false);
  const unenrollCourse = useUnenrollCourse();
  const handleUnenrollClick = () => {
    setIsUnenrollDialogOpen(true);
  };

  const handleConfirmUnenroll = async () => {
    await unenrollCourse.mutateAsync({
      studentId,
      courseId: courseId || '',
    });
    setIsUnenrollDialogOpen(false);
  };

  return (
    <>
      <Button variant={'destructive'} size='sm' onClick={handleUnenrollClick}>
        Unenroll
      </Button>

      <AlertDialog
        open={isUnenrollDialogOpen}
        onOpenChange={setIsUnenrollDialogOpen}
      >
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Confirm Unenrollment</AlertDialogTitle>
            <AlertDialogDescription>
              Are you sure you want to unenroll from this course? This action
              cannot be undone.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel disabled={unenrollCourse.isPending}>
              Cancel
            </AlertDialogCancel>
            <AlertDialogAction
              onClick={handleConfirmUnenroll}
              disabled={unenrollCourse.isPending}
              className='bg-destructive hover:bg-destructive/90'
            >
              {unenrollCourse.isPending ? (
                <>
                  <Loader2 className='mr-2 h-4 w-4 animate-spin' />
                  Unenrolling...
                </>
              ) : (
                'Unenroll'
              )}
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </>
  );
};

const CurrentEnrollmentsTable: React.FC<CurrentEnrollmentsTableProps> = ({
  studentId,
}) => {
  // Column definitions
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

  return (
    <div className='bg-white rounded-md p-4'>
      <GenericTable
        columns={columns}
        addAction={{ disabled: true, onAdd: () => {} }}
        queryHook={useEnrollments(studentId)}
        filterOptions={[]}
        customActionCellComponent={UnenrollButton}
        metadata={{
          studentId,
        }}
        emptyMessage='You are not currently enrolled in any courses.'
      />
    </div>
  );
};

export default CurrentEnrollmentsTable;
