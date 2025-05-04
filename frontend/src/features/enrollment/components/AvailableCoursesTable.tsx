import React, { useState } from 'react';
import { useCourses } from '@/features/course/api/useCourseApi';
import { useEnrollCourse } from '../api/useEnrollmentApi';
import { Button } from '@ui/button';

import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@ui/dialog';
import { Loader2 } from 'lucide-react';
import GenericTable from '@/components/table/GenericTable';
import Course from '@/features/course/types/course';
import { Column } from '@/core/types/table';

interface AvailableCoursesTableProps {
  studentId: string;
}

interface CustomEnrollButtonProps {
  id: string;
  studentId: string;
  code: string;
}

const CustomEnrollButton: React.FC<CustomEnrollButtonProps> = ({
  id,
  studentId,
}) => {
  const [isEnrollDialogOpen, setIsEnrollDialogOpen] = useState(false);
  const enrollCourse = useEnrollCourse();

  const handleEnrollClick = () => {
    setIsEnrollDialogOpen(true);
  };

  const handleConfirmEnroll = async () => {
    await enrollCourse.mutateAsync({
      studentId,
      courseId: id,
    });
    setIsEnrollDialogOpen(false);
  };

  return (
    <>
      <Button onClick={handleEnrollClick}>Enroll</Button>

      <Dialog open={isEnrollDialogOpen} onOpenChange={setIsEnrollDialogOpen}>
        <DialogContent className='max-w-[425px]'>
          <DialogHeader>
            <DialogTitle>Confirm Enrollment</DialogTitle>
            <DialogDescription>
              Are you sure you want to enroll in this course?
            </DialogDescription>
          </DialogHeader>
          <DialogFooter>
            <Button
              variant='outline'
              onClick={() => setIsEnrollDialogOpen(false)}
              disabled={enrollCourse.isPending}
            >
              Cancel
            </Button>
            <Button
              onClick={handleConfirmEnroll}
              disabled={enrollCourse.isPending}
            >
              {enrollCourse.isPending ? (
                <>
                  <Loader2 className='mr-2 h-4 w-4 animate-spin' />
                  Enrolling...
                </>
              ) : (
                'Enroll'
              )}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </>
  );
};

const AvailableCoursesTable: React.FC<AvailableCoursesTableProps> = ({
  studentId,
}) => {
  const columns: Column<Course>[] = [
    {
      header: 'Code',
      key: 'code',
    },
    {
      header: 'Subject',
      key: 'subject.name',
      nested: true,
    },
    {
      header: 'Schedule',
      key: 'schedule',
    },
    {
      header: 'Room',
      key: 'room',
    },
    {
      header: 'Semester',
      key: 'semester',
      transform: (value, row) => `${row?.year}, Semester ${value}`,
    },
  ];

  return (
    <GenericTable
      columns={columns}
      addAction={{
        disabled: true,
        onAdd: () => {},
      }}
      queryHook={useCourses}
      filterOptions={[]}
      customActionCellComponent={CustomEnrollButton}
      metadata={studentId}
    />
  );
};

export default AvailableCoursesTable;
