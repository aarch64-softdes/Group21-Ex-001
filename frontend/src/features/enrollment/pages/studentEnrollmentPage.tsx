import React, { useState } from 'react';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@ui/tabs';
import { useParams } from 'react-router-dom';
import { useStudent } from '@/features/student/api/useStudentApi';
import AvailableCoursesTable from '../components/AvailableCoursesTable';
import CurrentEnrollmentsTable from '../components/CurrentEnrollmentsTable';
import EnrollmentHistoryTable from '../components/EnrollmentHistoryTable';
import { Loader2 } from 'lucide-react';

const StudentEnrollmentPage: React.FC = () => {
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
      <div className='mb-6'>
        <h1 className='text-2xl font-bold mb-2'>Course Enrollment</h1>
        <p className='text-muted-foreground'>
          Student: {student.name} ({student.studentId})
        </p>
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
