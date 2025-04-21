import React, { useState } from 'react';
import { useStudents } from '@/features/student/api/useStudentApi';
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
import { Loader2, Search } from 'lucide-react';
import GenericTable from '@/components/table/GenericTable';
import { Column } from '@/core/types/table';
import { Badge } from '@ui/badge';
import { SearchFilterOption } from '@/core/types/filter';
import Student from '@/features/student/types/student';

interface AvailableStudentsTableProps {
  courseId: string;
}

interface CustomEnrollButtonProps {
  id: string;
  courseId: string;
}

const CustomEnrollButton: React.FC<CustomEnrollButtonProps> = ({
  id,
  courseId,
}) => {
  const [isEnrollDialogOpen, setIsEnrollDialogOpen] = useState(false);
  const enrollCourse = useEnrollCourse();

  const handleEnrollClick = () => {
    setIsEnrollDialogOpen(true);
  };

  const handleConfirmEnroll = async () => {
    await enrollCourse.mutateAsync({
      studentId: id,
      courseId,
    });
    setIsEnrollDialogOpen(false);
  };

  return (
    <>
      <Button size='sm' onClick={handleEnrollClick}>
        Enroll
      </Button>

      <Dialog open={isEnrollDialogOpen} onOpenChange={setIsEnrollDialogOpen}>
        <DialogContent className='max-w-[425px]'>
          <DialogHeader>
            <DialogTitle>Confirm Student Enrollment</DialogTitle>
            <DialogDescription>
              Are you sure you want to enroll this student in the course?
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

const AvailableStudentsTable: React.FC<AvailableStudentsTableProps> = ({
  courseId,
}) => {
  const columns: Column<Student>[] = [
    {
      header: 'Student ID',
      key: 'id',
    },
    {
      header: 'Name',
      key: 'name',
    },
    {
      header: 'Faculty',
      key: 'faculty',
    },
    {
      header: 'Program',
      key: 'program',
    },
    {
      header: 'Status',
      key: 'status',
      transform: (value) => (
        <Badge variant='outline' className='text-xs'>
          {value}
        </Badge>
      ),
    },
  ];

  const searchNameFilterOption: SearchFilterOption = {
    id: 'search',
    label: 'Search',
    labelIcon: Search,
    placeholder: 'Search by id, name',
    type: 'search',
  };

  return (
    <div className='bg-white rounded-md p-4'>
      <GenericTable
        columns={columns}
        addAction={{
          disabled: true,
          onAdd: () => {},
        }}
        queryHook={useStudents}
        filterOptions={[searchNameFilterOption]}
        customActionCellComponent={CustomEnrollButton}
        metadata={courseId}
      />
    </div>
  );
};

export default AvailableStudentsTable;
