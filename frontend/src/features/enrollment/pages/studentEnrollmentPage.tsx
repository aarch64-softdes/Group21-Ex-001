import React, { useState } from 'react';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@ui/tabs';
import { useNavigate, useParams } from 'react-router-dom';
import { useStudent } from '@/features/student/api/useStudentApi';
import AvailableCoursesTable from '../components/AvailableCoursesTable';
import CurrentEnrollmentsTable from '../components/CurrentEnrollmentsTable';
import EnrollmentHistoryTable from '../components/EnrollmentHistoryTable';
import { ChevronLeft, Loader2 } from 'lucide-react';
import { Button } from '@/components/ui/button';

const StudentEnrollmentPage: React.FC = () => {
  const navigate = useNavigate();
  const { studentId } = useParams<{ studentId: string }>();
  const { data: student, isLoading } = useStudent(studentId || '');
  const [activeTab, setActiveTab] = useState('available');

  if (isLoading) {
    return (
      <div className='flex items-center justify-center h-48'>
        <Loader2 className='h-8 w-8 animate-spin text-primary' />
      </div>
    );
  }

  if (!student || !studentId) {
    return (
      <div className='flex items-center justify-center h-48'>
        <p className='text-muted-foreground'>Student not found</p>
      </div>
    );
  }

  return (
    <div className='container mx-auto p-6'>
      <div className='mb-6 flex items-center justify-between'>
        <div>
          <h1 className='text-2xl font-bold mb-2'>Course Enrollment</h1>
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

      <Tabs value={activeTab} onValueChange={setActiveTab}>
        <TabsList className='mb-6'>
          <TabsTrigger value='available'>Available Courses</TabsTrigger>
          <TabsTrigger value='current'>Current Enrollments</TabsTrigger>
          <TabsTrigger value='history'>Enrollment History</TabsTrigger>
        </TabsList>

        <TabsContent value='available'>
          <AvailableCoursesTable studentId={studentId} />
        </TabsContent>

        <TabsContent value='current'>
          <CurrentEnrollmentsTable studentId={studentId} />
        </TabsContent>

        <TabsContent value='history'>
          <EnrollmentHistoryTable studentId={studentId} />
        </TabsContent>
      </Tabs>
    </div>
  );
};

export default StudentEnrollmentPage;
