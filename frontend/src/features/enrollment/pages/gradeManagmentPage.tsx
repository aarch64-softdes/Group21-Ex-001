import React from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useStudent } from '@/features/student/api/useStudentApi';
import GradeManagement from '../components/GradeManagement';
import { Button } from '@ui/button';
import { ChevronLeft, ExternalLink, Loader2 } from 'lucide-react';
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@ui/card';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@ui/tabs';
import { ResourceNotFoundError } from '@/shared/lib/errors';

const GradeManagementPage: React.FC = () => {
  const { studentId } = useParams<{ studentId: string }>();
  const navigate = useNavigate();
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
        <div className='flex justify-between items-start mb-6'>
          <div>
            <h1 className='text-2xl font-bold'>Grade Management</h1>
            <p className='text-muted-foreground'>
              Student: {student.name} ({student.studentId})
            </p>
          </div>
          <Button
            variant='outline'
            onClick={() => navigate(`/student/${studentId}/transcript`)}
          >
            <ExternalLink className='h-4 w-4 mr-2' />
            View Transcript
          </Button>
        </div>
      </div>

      <Card className='mb-6'>
        <CardHeader className='pb-3'>
          <CardTitle>Student Information</CardTitle>
          <CardDescription>Basic details about the student</CardDescription>
        </CardHeader>
        <CardContent>
          <div className='grid grid-cols-1 md:grid-cols-3 gap-6'>
            <div>
              <h3 className='text-sm font-medium text-muted-foreground'>
                Personal Details
              </h3>
              <div className='mt-2 space-y-1'>
                <p>
                  <strong>Name:</strong> {student.name}
                </p>
                <p>
                  <strong>Student ID:</strong> {student.studentId}
                </p>
                <p>
                  <strong>Email:</strong> {student.email}
                </p>
                <p>
                  <strong>Gender:</strong> {student.gender}
                </p>
              </div>
            </div>

            <div>
              <h3 className='text-sm font-medium text-muted-foreground'>
                Academic Information
              </h3>
              <div className='mt-2 space-y-1'>
                <p>
                  <strong>Faculty:</strong> {student.faculty}
                </p>
                <p>
                  <strong>Program:</strong> {student.program}
                </p>
                <p>
                  <strong>School Year:</strong> {student.schoolYear}
                </p>
                <p>
                  <strong>Status:</strong> {student.status}
                </p>
              </div>
            </div>

            <div>
              <h3 className='text-sm font-medium text-muted-foreground'>
                Contact Information
              </h3>
              <div className='mt-2 space-y-1'>
                <p>
                  <strong>Phone:</strong> {student.phone?.phoneNumber}
                </p>
                <p>
                  <strong>Country Code:</strong> {student.phone?.countryCode}
                </p>
              </div>
            </div>
          </div>
        </CardContent>
      </Card>

      <h2 className='text-xl font-bold'>Manage Grades</h2>

      <GradeManagement studentId={studentId} />
    </div>
  );
};

export default GradeManagementPage;
