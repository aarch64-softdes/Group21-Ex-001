import React, { useState } from 'react';
import {
  useEnrollments,
  useUnenrollCourse,
  useUpdateTranscript,
} from '../api/useEnrollmentApi';
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
import {
  AlertTriangle,
  CheckCircle2,
  Clock,
  Edit2,
  Loader2,
  XCircle,
} from 'lucide-react';
import TablePagination from '@/components/table/TablePagination';
import { Badge } from '@ui/badge';
import { UpdateTranscriptDTO } from '../types/enrollment';
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
import { Input } from '@ui/input';
import { Label } from '@ui/label';

interface CurrentEnrollmentsTableProps {
  studentId: string;
  isAdmin?: boolean;
}

const CurrentEnrollmentsTable: React.FC<CurrentEnrollmentsTableProps> = ({
  studentId,
  isAdmin = false,
}) => {
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [selectedCourse, setSelectedCourse] = useState<string | null>(null);
  const [isUnenrollDialogOpen, setIsUnenrollDialogOpen] = useState(false);
  const [isEditGradeDialogOpen, setIsEditGradeDialogOpen] = useState(false);
  const [gradeInput, setGradeInput] = useState('');
  const [gpaInput, setGpaInput] = useState('');

  const { data: enrollmentsResponse, isLoading } = useEnrollments(
    studentId,
    page,
    pageSize,
  );

  const unenrollCourse = useUnenrollCourse();
  const updateTranscript = useUpdateTranscript();

  const handleUnenrollClick = (courseId: string) => {
    setSelectedCourse(courseId);
    setIsUnenrollDialogOpen(true);
  };

  const handleEditGradeClick = (
    courseId: string,
    grade?: string,
    gpa?: number,
  ) => {
    setSelectedCourse(courseId);
    setGradeInput(grade || '');
    setGpaInput(gpa?.toString() || '');
    setIsEditGradeDialogOpen(true);
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

  const handleSaveGrade = async () => {
    if (selectedCourse) {
      const data: UpdateTranscriptDTO = {
        studentId,
        courseId: selectedCourse,
        transcript: {
          grade: gradeInput,
          gpa: parseFloat(gpaInput) || 0,
        },
      };

      await updateTranscript.mutateAsync(data);
      setIsEditGradeDialogOpen(false);
      setSelectedCourse(null);
    }
  };

  const getGradeDisplay = (grade?: string, gpa?: number) => {
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
            <TableHead>Grade</TableHead>
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
                {getGradeDisplay(
                  enrollment.score?.grade,
                  enrollment.score?.gpa,
                )}
              </TableCell>
              <TableCell>
                <div className='flex space-x-2'>
                  {isAdmin && (
                    <Button
                      size='sm'
                      variant='outline'
                      onClick={() =>
                        handleEditGradeClick(
                          enrollment.course.id!,
                          enrollment.score?.grade,
                          enrollment.score?.gpa,
                        )
                      }
                      disabled={updateTranscript.isPending}
                    >
                      <Edit2 className='h-4 w-4 mr-1' />
                      Grade
                    </Button>
                  )}
                  <Button
                    size='sm'
                    variant='destructive'
                    onClick={() => handleUnenrollClick(enrollment.course.id!)}
                    disabled={unenrollCourse.isPending}
                  >
                    Unenroll
                  </Button>
                </div>
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

      {/* Unenroll Dialog */}
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
              className='bg-destructive text-destructive-foreground hover:bg-destructive/90'
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

      {/* Edit Grade Dialog */}
      <Dialog
        open={isEditGradeDialogOpen}
        onOpenChange={setIsEditGradeDialogOpen}
      >
        <DialogContent className='sm:max-w-[425px]'>
          <DialogHeader>
            <DialogTitle>Update Grade</DialogTitle>
            <DialogDescription>
              Enter the grade and GPA for this course enrollment.
            </DialogDescription>
          </DialogHeader>
          <div className='grid gap-4 py-4'>
            <div className='grid grid-cols-4 items-center gap-4'>
              <Label htmlFor='grade' className='text-right'>
                Grade
              </Label>
              <Input
                id='grade'
                value={gradeInput}
                onChange={(e) => setGradeInput(e.target.value)}
                className='col-span-3'
                placeholder='A, B+, C, etc.'
              />
            </div>
            <div className='grid grid-cols-4 items-center gap-4'>
              <Label htmlFor='gpa' className='text-right'>
                GPA
              </Label>
              <Input
                id='gpa'
                type='number'
                step='0.01'
                min='0'
                max='4.0'
                value={gpaInput}
                onChange={(e) => setGpaInput(e.target.value)}
                className='col-span-3'
                placeholder='0.00-4.00'
              />
            </div>
          </div>
          <DialogFooter>
            <Button
              variant='outline'
              onClick={() => setIsEditGradeDialogOpen(false)}
              disabled={updateTranscript.isPending}
            >
              Cancel
            </Button>
            <Button
              onClick={handleSaveGrade}
              disabled={updateTranscript.isPending}
            >
              {updateTranscript.isPending ? (
                <>
                  <Loader2 className='mr-2 h-4 w-4 animate-spin' />
                  Saving...
                </>
              ) : (
                'Save Changes'
              )}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default CurrentEnrollmentsTable;
