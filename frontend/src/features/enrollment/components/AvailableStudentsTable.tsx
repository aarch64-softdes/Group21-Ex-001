// src/features/enrollment/components/AvailableStudentsTable.tsx
import React, { useState, useEffect } from 'react';
import { useStudents } from '@/features/student/api/useStudentApi';
import { useEnrollCourse } from '../api/useEnrollmentApi';
import { Button } from '@ui/button';
import { Input } from '@ui/input';
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
import { Loader2, Search } from 'lucide-react';
import TablePagination from '@/components/table/TablePagination';
import { Badge } from '@ui/badge';
import TableSkeleton from '@/components/table/SkeletonTable';

interface AvailableStudentsTableProps {
  courseId: string;
}

const AvailableStudentsTable: React.FC<AvailableStudentsTableProps> = ({
  courseId,
}) => {
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [selectedStudent, setSelectedStudent] = useState<string | null>(null);
  const [isEnrollDialogOpen, setIsEnrollDialogOpen] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  const [debouncedSearchTerm, setDebouncedSearchTerm] = useState('');

  // Effect for debouncing search
  useEffect(() => {
    const timer = setTimeout(() => {
      setDebouncedSearchTerm(searchTerm);
    }, 300);

    return () => {
      clearTimeout(timer);
    };
  }, [searchTerm]);

  const {
    data: studentsResponse,
    isLoading,
    error,
    refetch,
  } = useStudents({
    page,
    pageSize,
    filters: { search: debouncedSearchTerm },
    sort: { key: 'studentId', direction: 'asc' },
  });

  const enrollCourse = useEnrollCourse();

  const handleEnrollClick = (studentId: string) => {
    setSelectedStudent(studentId);
    setIsEnrollDialogOpen(true);
  };

  const handleConfirmEnroll = async () => {
    if (selectedStudent) {
      await enrollCourse.mutateAsync({
        studentId: selectedStudent,
        courseId,
      });
      setIsEnrollDialogOpen(false);
      setSelectedStudent(null);
    }
  };

  if (isLoading) {
    return <TableSkeleton columns={6} rows={5} />;
  }

  if (error) {
    throw error;
  }

  return (
    <div className='bg-white rounded-md p-4'>
      <div className='mb-4 relative'>
        <div className='absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none'>
          <Search className='h-4 w-4 text-muted-foreground' />
        </div>
        <Input
          type='search'
          placeholder='Search students by name or ID...'
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className='pl-10'
        />
      </div>

      {!studentsResponse?.data || studentsResponse.data.length === 0 ? (
        <div className='text-center py-8'>
          <p className='text-muted-foreground'>No available students found.</p>
        </div>
      ) : (
        <>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Student ID</TableHead>
                <TableHead>Name</TableHead>
                <TableHead>Faculty</TableHead>
                <TableHead>Program</TableHead>
                <TableHead>Status</TableHead>
                <TableHead>Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {studentsResponse.data.map((student) => (
                <TableRow key={student.id}>
                  <TableCell className='font-medium'>
                    {student.studentId}
                  </TableCell>
                  <TableCell>{student.name}</TableCell>
                  <TableCell>{student.faculty}</TableCell>
                  <TableCell>{student.program}</TableCell>
                  <TableCell>
                    <Badge variant='outline' className='text-xs'>
                      {student.status}
                    </Badge>
                  </TableCell>
                  <TableCell>
                    <Button
                      size='sm'
                      onClick={() => handleEnrollClick(student.id)}
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
              totalPages={studentsResponse.totalPages}
              totalItems={studentsResponse.totalItems}
              pageSize={pageSize}
              onPageChange={setPage}
              onPageSizeChange={setPageSize}
            />
          </div>
        </>
      )}

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
    </div>
  );
};

export default AvailableStudentsTable;
