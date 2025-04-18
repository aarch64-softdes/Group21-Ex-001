import React, { useState } from 'react';
import { useCourses } from '@/features/course/api/useCourseApi';
import { useEnrollCourse } from '../api/useEnrollmentApi';
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

interface AvailableCoursesTableProps {
  studentId: string;
}

const AvailableCoursesTable: React.FC<AvailableCoursesTableProps> = ({
  studentId,
}) => {
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [selectedCourse, setSelectedCourse] = useState<string | null>(null);
  const [isEnrollDialogOpen, setIsEnrollDialogOpen] = useState(false);

  const { data: coursesResponse, isLoading } = useCourses({
    page,
    pageSize,
    filters: {},
    sort: { key: 'code', direction: 'asc' },
  });

  const enrollCourse = useEnrollCourse();

  const handleEnrollClick = (courseId: string) => {
    setSelectedCourse(courseId);
    setIsEnrollDialogOpen(true);
  };

  const handleConfirmEnroll = async () => {
    if (selectedCourse) {
      await enrollCourse.mutateAsync({
        studentId,
        courseId: selectedCourse,
      });
      setIsEnrollDialogOpen(false);
      setSelectedCourse(null);
    }
  };

  // Format semester display
  const formatSemester = (year: number, semester: number) => {
    return `${year}, Semester ${semester}`;
  };

  if (isLoading) {
    return (
      <div className='flex items-center justify-center h-48'>
        <Loader2 className='h-8 w-8 animate-spin text-primary' />
      </div>
    );
  }

  return (
    <div className='bg-white rounded-md p-4'>
      {!coursesResponse?.data || coursesResponse.data.length === 0 ? (
        <div className='text-center py-8'>
          <p className='text-muted-foreground'>No available courses found.</p>
        </div>
      ) : (
        <>
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
              {coursesResponse.data.map((course) => (
                <TableRow key={course.id}>
                  <TableCell className='font-medium'>{course.code}</TableCell>
                  <TableCell>{course.subject?.name}</TableCell>
                  <TableCell>{course.schedule}</TableCell>
                  <TableCell>{course.room}</TableCell>
                  <TableCell>
                    {formatSemester(course.year, course.semester)}
                  </TableCell>
                  <TableCell>
                    <Button
                      size='sm'
                      onClick={() => handleEnrollClick(course.id)}
                      disabled={enrollCourse.isPending}
                    >
                      Enroll
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>

          <div className='py-4'>
            <TablePagination
              currentPage={page}
              totalPages={coursesResponse.totalPages}
              totalItems={coursesResponse.totalItems}
              pageSize={pageSize}
              onPageChange={setPage}
              onPageSizeChange={setPageSize}
            />
          </div>
        </>
      )}

      <Dialog open={isEnrollDialogOpen} onOpenChange={setIsEnrollDialogOpen}>
        <DialogContent>
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
    </div>
  );
};

export default AvailableCoursesTable;
