import React, { useState } from 'react';
import { useEnrollments, useUpdateTranscript } from '../api/useEnrollmentApi';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@ui/table';
import { Button } from '@ui/button';
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
  DialogFooter,
} from '@ui/dialog';
import { Input } from '@ui/input';
import { Label } from '@ui/label';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@ui/select';
import { Loader2, Save, AlertTriangle } from 'lucide-react';
import { UpdateTranscriptDTO } from '../types/enrollment';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@ui/tabs';
import TablePagination from '@/components/table/TablePagination';
import { Badge } from '@ui/badge';
import { Alert, AlertDescription, AlertTitle } from '@ui/alert';

interface GradeManagementProps {
  studentId: string;
}

// Grade scale mapping
const GRADE_SCALE = {
  'A+': 4.0,
  A: 4.0,
  'A-': 3.7,
  'B+': 3.3,
  B: 3.0,
  'B-': 2.7,
  'C+': 2.3,
  C: 2.0,
  'C-': 1.7,
  'D+': 1.3,
  D: 1.0,
  F: 0.0,
};

const GradeManagement: React.FC<GradeManagementProps> = ({ studentId }) => {
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [selectedCourse, setSelectedCourse] = useState<string | null>(null);
  const [isEditGradeDialogOpen, setIsEditGradeDialogOpen] = useState(false);
  const [gradeInput, setGradeInput] = useState('');
  const [gpaInput, setGpaInput] = useState('');
  const [editMode, setEditMode] = useState<'manual' | 'scale'>('scale');

  const { data: enrollmentsResponse, isLoading } = useEnrollments(
    studentId,
    page,
    pageSize,
  );

  const updateTranscript = useUpdateTranscript();

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

  const handleGradeSelect = (value: string) => {
    setGradeInput(value);
    // Auto-set GPA based on grade scale
    const gpa = GRADE_SCALE[value as keyof typeof GRADE_SCALE] || '';
    setGpaInput(gpa.toString());
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

  const getGradeStatus = (grade?: string, gpa?: number) => {
    if (!grade && !gpa) return 'pending';
    if (gpa !== undefined) {
      if (gpa >= 3.5) return 'excellent';
      if (gpa >= 2.0) return 'pass';
      return 'fail';
    }
    return 'pending';
  };

  const getGradeBadge = (status: string) => {
    switch (status) {
      case 'excellent':
        return (
          <Badge className='bg-green-100 text-green-800 hover:bg-green-200'>
            Excellent
          </Badge>
        );
      case 'pass':
        return (
          <Badge className='bg-blue-100 text-blue-800 hover:bg-blue-200'>
            Pass
          </Badge>
        );
      case 'fail':
        return (
          <Badge className='bg-red-100 text-red-800 hover:bg-red-200'>
            Fail
          </Badge>
        );
      default:
        return <Badge variant='outline'>Pending</Badge>;
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
          No enrollments found for this student.
        </p>
      </div>
    );
  }

  return (
    <div className='bg-white rounded-md p-4'>
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Course Code</TableHead>
            <TableHead>Subject</TableHead>
            <TableHead>Semester</TableHead>
            <TableHead>Current Grade</TableHead>
            <TableHead>Current GPA</TableHead>
            <TableHead>Status</TableHead>
            <TableHead>Actions</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {enrollmentsResponse.data.map((enrollment) => {
            const status = getGradeStatus(
              enrollment.score?.grade,
              enrollment.score?.gpa,
            );
            return (
              <TableRow key={enrollment.id}>
                <TableCell className='font-medium'>
                  {enrollment.course.code}
                </TableCell>
                <TableCell>{enrollment.course.subject?.name}</TableCell>
                <TableCell>
                  {enrollment.course.year}, Semester{' '}
                  {enrollment.course.semester}
                </TableCell>
                <TableCell>{enrollment.score?.grade || 'N/A'}</TableCell>
                <TableCell>
                  {enrollment.score?.gpa !== undefined
                    ? enrollment.score.gpa.toFixed(2)
                    : 'N/A'}
                </TableCell>
                <TableCell>{getGradeBadge(status)}</TableCell>
                <TableCell>
                  <Button
                    size='sm'
                    onClick={() =>
                      handleEditGradeClick(
                        enrollment.course.id!,
                        enrollment.score?.grade,
                        enrollment.score?.gpa,
                      )
                    }
                    disabled={updateTranscript.isPending}
                  >
                    <Save className='h-4 w-4 mr-1' />
                    Update Grade
                  </Button>
                </TableCell>
              </TableRow>
            );
          })}
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

      {/* Edit Grade Dialog */}
      <Dialog
        open={isEditGradeDialogOpen}
        onOpenChange={setIsEditGradeDialogOpen}
      >
        <DialogContent className='sm:max-w-[500px]'>
          <DialogHeader>
            <DialogTitle>Update Grade</DialogTitle>
            <DialogDescription>
              Enter the grade and GPA for this course enrollment.
            </DialogDescription>
          </DialogHeader>

          <Tabs
            value={editMode}
            onValueChange={(value) => setEditMode(value as 'manual' | 'scale')}
          >
            <TabsList className='grid w-full grid-cols-2 mb-4'>
              <TabsTrigger value='scale'>Grade Scale</TabsTrigger>
              <TabsTrigger value='manual'>Manual Entry</TabsTrigger>
            </TabsList>

            <TabsContent value='scale'>
              <div className='grid gap-4 py-4'>
                <div className='grid grid-cols-4 items-center gap-4'>
                  <Label htmlFor='grade-select' className='text-right'>
                    Grade
                  </Label>
                  <Select value={gradeInput} onValueChange={handleGradeSelect}>
                    <SelectTrigger className='col-span-3'>
                      <SelectValue placeholder='Select grade' />
                    </SelectTrigger>
                    <SelectContent>
                      {Object.keys(GRADE_SCALE).map((grade) => (
                        <SelectItem key={grade} value={grade}>
                          {grade} (
                          {GRADE_SCALE[
                            grade as keyof typeof GRADE_SCALE
                          ].toFixed(1)}
                          )
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
                <div className='grid grid-cols-4 items-center gap-4'>
                  <Label htmlFor='gpa-display' className='text-right'>
                    GPA (auto)
                  </Label>
                  <Input
                    id='gpa-display'
                    value={gpaInput}
                    readOnly
                    className='col-span-3 bg-gray-50'
                  />
                </div>
              </div>
            </TabsContent>

            <TabsContent value='manual'>
              <div className='grid gap-4 py-4'>
                <div className='grid grid-cols-4 items-center gap-4'>
                  <Label htmlFor='grade-manual' className='text-right'>
                    Grade
                  </Label>
                  <Input
                    id='grade-manual'
                    value={gradeInput}
                    onChange={(e) => setGradeInput(e.target.value)}
                    className='col-span-3'
                    placeholder='A, B+, C, etc.'
                  />
                </div>
                <div className='grid grid-cols-4 items-center gap-4'>
                  <Label htmlFor='gpa-manual' className='text-right'>
                    GPA
                  </Label>
                  <Input
                    id='gpa-manual'
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
            </TabsContent>
          </Tabs>

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
              disabled={updateTranscript.isPending || !gradeInput || !gpaInput}
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

export default GradeManagement;
