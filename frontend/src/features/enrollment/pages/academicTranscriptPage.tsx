import React from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useStudent } from '@/features/student/api/useStudentApi';
import AcademicTranscript from '@enrollment/components/AcademicTranscriptView';
import { ChevronLeft, Loader2 } from 'lucide-react';
import { ResourceNotFoundError } from '@/shared/lib/errors';
import { Button } from '@/components/ui/button';

const AcademicTranscriptPage: React.FC = () => {
  const navigate = useNavigate();
  const { studentId } = useParams<{ studentId: string }>();
  const { data: student, isLoading, error } = useStudent(studentId || '');

  if (isLoading) {
    return (
      <div className='flex items-center justify-center h-48'>
        <Loader2 className='h-8 w-8 animate-spin text-primary' />
      </div>
    );
  }

  if (error) {
    throw error;
  }

  if (!student || !studentId) {
    throw new ResourceNotFoundError(`Student with ID ${studentId} not found.`);
  }

  return (
    <div className='container mx-auto p-6'>
      <div className='mb-6 flex items-center justify-between'>
        <div>
          <h1 className='text-2xl font-bold'>Academic Transcript</h1>
          <p className='text-muted-foreground'>
            Student: {student.name} ({student.studentId})
          </p>
        </div>
        <Button
          className='w-32 mr-4'
          variant='outline'
          onClick={() => {
            navigate('/student');
          }}
        >
          <ChevronLeft className='mr-2 h-4 w-4' />
          Return
        </Button>
      </div>

      <AcademicTranscript studentId={studentId} />
    </div>
  );
};

export default AcademicTranscriptPage;
