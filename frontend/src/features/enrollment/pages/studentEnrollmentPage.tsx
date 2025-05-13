import React, { useEffect } from 'react';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@ui/tabs';
import { useNavigate, useParams, useSearchParams } from 'react-router-dom';
import { useStudent } from '@/features/student/api/useStudentApi';
import AvailableCoursesTable from '../components/AvailableCoursesTable';
import CurrentEnrollmentsTable from '../components/CurrentEnrollmentsTable';
import EnrollmentHistoryTable from '../components/EnrollmentHistoryTable';
import { ChevronLeft, Loader2 } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { ResourceNotFoundError } from '@/shared/lib/errors';
import AcademicTranscript from '../components/AcademicTranscriptView';
import { useTranslation } from 'react-i18next';

const StudentEnrollmentPage: React.FC = () => {
  const navigate = useNavigate();
  const { t } = useTranslation(['enrollment', 'common']);
  const { studentId } = useParams<{ studentId: string }>();
  const { data: student, isLoading, error } = useStudent(studentId || '');

  // Use search params to store and retrieve the active tab
  const [searchParams, setSearchParams] = useSearchParams();
  const activeTab = searchParams.get('tab') || 'available';

  // Update the URL when tab changes
  const handleTabChange = (value: string) => {
    setSearchParams({ tab: value });
  };

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
    throw new ResourceNotFoundError('Student not found');
  }

  return (
    <div className='container mx-auto p-6'>
      <div className='mb-6 flex items-center justify-between'>
        <div>
          <h1 className='text-2xl font-bold mb-2'>{t('title')}</h1>
          <p className='text-muted-foreground'>
            {t('student')}: {student.name} ({student.studentId})
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
          {t('return')}
        </Button>
      </div>

      <Tabs value={activeTab} onValueChange={handleTabChange}>
        <TabsList className='mb-6'>
          <TabsTrigger value='available'>Available Courses</TabsTrigger>
          <TabsTrigger value='current'>Current Enrollments</TabsTrigger>
          <TabsTrigger value='transcript'>Transcript</TabsTrigger>
          <TabsTrigger value='enrollmentHistory'>Enrollment History</TabsTrigger>
        </TabsList>

        <TabsContent value='available'>
          <AvailableCoursesTable studentId={studentId} />
        </TabsContent>

        <TabsContent value='current'>
          <CurrentEnrollmentsTable studentId={studentId} />
        </TabsContent>

        <TabsContent value='enrollmentHistory'>
          <EnrollmentHistoryTable studentId={studentId} />
        </TabsContent>

        <TabsContent value='transcript'>
          <AcademicTranscript studentId={studentId} />
        </TabsContent>
      </Tabs>
    </div>
  );
};

export default StudentEnrollmentPage;
