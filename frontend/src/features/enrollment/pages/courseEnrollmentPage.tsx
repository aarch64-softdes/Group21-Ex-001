import React from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useCourse } from '@/features/course/api/useCourseApi';
import AvailableStudentsTable from '../components/AvailableStudentsTable';
import { ChevronLeft, Loader2 } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { ResourceNotFoundError } from '@/shared/lib/errors';
import { useTranslation } from 'react-i18next';

const CourseEnrollmentPage: React.FC = () => {
  const navigate = useNavigate();
  const { t } = useTranslation(['enrollment', 'common']);
  const { courseId } = useParams<{ courseId: string }>();
  const { data: course, isLoading, error } = useCourse(courseId || '');

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

  if (!course || !courseId) {
    throw new ResourceNotFoundError('Course not found');
  }

  return (
    <div className='container mx-auto p-6'>
      <div className='mb-6 flex items-center justify-between'>
        <div>
          <h1 className='text-2xl font-bold mb-2'>{t('title')}</h1>
          <p className='text-muted-foreground'>
            {t('course:fields.subject')}: {course.subject?.name} ({course.code})
          </p>
          <p className='text-sm text-muted-foreground'>
            {t('course:fields.semester')}: {course.year},{' '}
            {t('course:fields.semester')} {course.semester} |{' '}
            {t('course:fields.room')}: {course.room} |{' '}
            {t('course:fields.schedule')}: {course.schedule}
          </p>
        </div>

        <Button
          className='w-32 mr-4'
          variant='outline'
          onClick={() => {
            navigate('/course');
          }}
        >
          <ChevronLeft className='mr-2 h-4 w-4' />
          {t('return')}
        </Button>
      </div>

      <AvailableStudentsTable courseId={courseId} />
    </div>
  );
};

export default CourseEnrollmentPage;
