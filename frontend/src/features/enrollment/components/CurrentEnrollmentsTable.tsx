import React, { useState } from 'react';
import { useEnrollments, useUnenrollCourse } from '../api/useEnrollmentApi';
import { Button } from '@ui/button';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@ui/table';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@ui/dialog';
import { Loader2 } from 'lucide-react';
import TablePagination from '@/components/table/TablePagination';

interface CurrentEnrollmentsTableProps {
  studentId: string;
}

const CurrentEnrollmentsTable: React.FC<CurrentEnrollmentsTableProps> = ({
  studentId,
}) => {
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [selectedCourse, setSelectedCourse] = useState<string | null>(null);
  const [isUnenrollDialogOpen, setIsUnenrollDialogOpen] = useState(false);

  const { data: enrollmentsResponse, isLoading } = useEnrollments(
    studentId,
    page,
    pageSize,
  );

  const unenrollCourse = useUnenrollCourse();

  const handleUnenrollClick = (courseId: string) => {
    setSelectedCourse(courseId);
    setIsUnenrollDialogOpen(true);
  };

  const handleConfirmUnenroll = async () => {
    if (selectedCourse) {
      await unenrollCourse.mutateAsync({
        studentId,
        courseId: selectedCourse,
      });
      setIsUnenrollDialogOpen(false);
      setSelectedCourse(null);
    }
  };

  if (isLoading) {
    return (
      <div className='flex items-center justify-center h-48'>
        <Loader2 className='h-8 w-8 animate-spin text-primary' />
      </div>
    );
  }

  if (!enrollmentsResponse?.data || enrollmentsResponse.data.length === 0) {
    return (
      <div className='bg-white rounded-md p-6 text-center'>
        <p className='text-muted-foreground'>
          You are not currently enrolled in any courses.
        </p>
      </div>
    );
  }

  return (
    <div className='bg-white rounded-md p-4'>
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Code</TableHead>
            <TableHead>Subject</TableHead>
            <TableHead>Schedule</TableHead>
            <TableHead>Room</TableHead>
            <TableHead>Semester</TableHead>
            <TableHead>Actions</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {enrollmentsResponse.data.map((enrollment) => (
            <TableRow key={enrollment.id}>
              <TableCell className='font-medium'>
                {enrollment.course.code}
              </TableCell>
              <TableCell>{enrollment.course.subject?.name}</TableCell>
              <TableCell>{enrollment.course.schedule}</TableCell>
              <TableCell>{enrollment.course.room}</TableCell>
              <TableCell>
                {enrollment.course.year}, Semester {enrollment.course.semester}
              </TableCell>
              <TableCell>
                <Button
                  size='sm'
                  variant='destructive'
                  onClick={() => handleUnenrollClick(enrollment.course.id!)}
                  disabled={unenrollCourse.isPending}
                >
                  Unenroll
                </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>

      <div className='py-4'>
        <TablePagination
          currentPage={page}
          totalPages={enrollmentsResponse.totalPages}
          totalItems={enrollmentsResponse.totalItems}
          pageSize={pageSize}
          onPageChange={setPage}
          onPageSizeChange={setPageSize}
        />
      </div>

      <Dialog
        open={isUnenrollDialogOpen}
        onOpenChange={setIsUnenrollDialogOpen}
      >
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Confirm Unenrollment</DialogTitle>
            <DialogDescription>
              Are you sure you want to unenroll from this course? This action
              cannot be undone.
            </DialogDescription>
          </DialogHeader>
          <DialogFooter>
            <Button
              variant='outline'
              onClick={() => setIsUnenrollDialogOpen(false)}
              disabled={unenrollCourse.isPending}
            >
              Cancel
            </Button>
            <Button
              variant='destructive'
              onClick={handleConfirmUnenroll}
              disabled={unenrollCourse.isPending}
            >
              {unenrollCourse.isPending ? (
                <>
                  <Loader2 className='mr-2 h-4 w-4 animate-spin' />
                  Unenrolling...
                </>
              ) : (
                'Unenroll'
              )}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default CurrentEnrollmentsTable;
