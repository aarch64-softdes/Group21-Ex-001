import React from 'react';
import { useParams } from 'react-router-dom';
import { useStudent } from '@/features/student/api/useStudentApi';
import AcademicTranscript from '@enrollment/components/AcademicTranscriptView';
import { Loader2 } from 'lucide-react';
import { ResourceNotFoundError } from '@/shared/lib/errors';

const AcademicTranscriptPage: React.FC = () => {
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
      <div className='mb-6'>
        <h1 className='text-2xl font-bold'>Academic Transcript</h1>
        <p className='text-muted-foreground'>
          Student: {student.name} ({student.studentId})
        </p>
      </div>

      <AcademicTranscript studentId={studentId} />
    </div>
  );
};

export default AcademicTranscriptPage;
