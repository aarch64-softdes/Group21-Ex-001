import React from 'react';
import { useParams } from 'react-router-dom';
import { useCourse } from '@/features/course/api/useCourseApi';
import AvailableStudentsTable from './components/AvailableStudentsTable';
import { Loader2 } from 'lucide-react';

const CourseEnrollmentPage: React.FC = () => {
  const { courseId } = useParams<{ courseId: string }>();
  const { data: course, isLoading } = useCourse(courseId || '');

  if (isLoading) {
    return (
      <div className='flex items-center justify-center h-48'>
        <Loader2 className='h-8 w-8 animate-spin text-primary' />
      </div>
    );
  }

  if (!course || !courseId) {
    return (
      <div className='flex items-center justify-center h-48'>
        <p className='text-muted-foreground'>Course not found</p>
      </div>
    );
  }

  return (
    <div className='container mx-auto p-6'>
      <div className='mb-6'>
        <h1 className='text-2xl font-bold mb-2'>Course Student Management</h1>
        <p className='text-muted-foreground'>
          Course: {course.subject?.name} ({course.code})
        </p>
        <p className='text-sm text-muted-foreground'>
          Semester: {course.year}, Semester {course.semester} | Room:{' '}
          {course.room} | Schedule: {course.schedule}
        </p>
      </div>

      <AvailableStudentsTable courseId={courseId} />
    </div>
  );
};

export default CourseEnrollmentPage;
