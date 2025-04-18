import React, { useRef } from 'react';
import { useAcademicTranscript } from '../api/useEnrollmentApi';
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from '@ui/card';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@ui/table';
import { Badge } from '@ui/badge';
import { Loader2, Download, Printer } from 'lucide-react';
import { Button } from '@ui/button';
import { Transcript } from '../types/enrollment';
import { format } from 'date-fns';
import { Separator } from '@ui/separator';

// Import the print styles
import '../styles/transcript-print.css';

interface AcademicTranscriptProps {
  studentId: string;
}

const AcademicTranscript: React.FC<AcademicTranscriptProps> = ({
  studentId,
}) => {
  const transcriptRef = useRef<HTMLDivElement>(null);
  const {
    data: transcript,
    isLoading,
    error,
  } = useAcademicTranscript(studentId);

  const getGradeDisplayClass = (gpa: number) => {
    if (gpa >= 3.5) return 'text-green-600';
    if (gpa >= 2.0) return 'text-amber-500';
    return 'text-red-500';
  };

  const handlePrint = () => {
    // Add a class to the body before printing
    document.body.classList.add('print-transcript-only');

    // Call the print function
    window.print();

    // Remove the class after printing dialog closes
    setTimeout(() => {
      document.body.classList.remove('print-transcript-only');
    }, 500);
  };

  // This would be implemented fully with a proper API endpoint
  const handleDownload = () => {
    // In a real implementation, this would call an API endpoint to generate a PDF
    alert('This feature would download a PDF of the transcript.');
  };

  if (isLoading) {
    return (
      <div className='flex items-center justify-center h-60'>
        <Loader2 className='h-8 w-8 animate-spin text-primary' />
      </div>
    );
  }

  if (error) {
    return (
      <div className='bg-white rounded-md p-6 text-center'>
        <p className='text-red-500'>Failed to load transcript data.</p>
      </div>
    );
  }

  if (!transcript) {
    return (
      <div className='bg-white rounded-md p-6 text-center'>
        <p className='text-muted-foreground'>No transcript data available.</p>
      </div>
    );
  }

  return (
    <Card
      className='max-w-4xl mx-auto print:shadow-none'
      id='transcript-printable'
      ref={transcriptRef}
    >
      <CardHeader className='print:pb-2'>
        <div className='flex justify-between items-start'>
          <div>
            <CardTitle className='text-2xl'>Academic Transcript</CardTitle>
            <CardDescription>Official student transcript</CardDescription>
          </div>
          <div className='flex space-x-2 print-hidden'>
            <Button variant='outline' size='sm' onClick={handlePrint}>
              <Printer className='h-4 w-4 mr-2' />
              Print
            </Button>
            <Button variant='outline' size='sm' onClick={handleDownload}>
              <Download className='h-4 w-4 mr-2' />
              Download
            </Button>
          </div>
        </div>
      </CardHeader>

      <CardContent className='space-y-6'>
        <div className='grid grid-cols-2 gap-4'>
          <div>
            <h3 className='text-sm font-medium text-muted-foreground'>
              Student Information
            </h3>
            <div className='mt-2 space-y-1'>
              <p>
                <strong>Name:</strong> {transcript.studentName}
              </p>
              <p>
                <strong>ID:</strong> {transcript.studentId}
              </p>
              <p>
                <strong>Date of Birth:</strong>{' '}
                {transcript.studentDob
                  ? format(new Date(transcript.studentDob), 'PPP')
                  : 'N/A'}
              </p>
              <p>
                <strong>Program:</strong> {transcript.courseName || 'N/A'}
              </p>
            </div>
          </div>

          <div>
            <h3 className='text-sm font-medium text-muted-foreground'>
              Academic Summary
            </h3>
            <div className='mt-2 space-y-1'>
              <p className='flex items-center justify-between'>
                <span>
                  <strong>Cumulative GPA:</strong>
                </span>
                <Badge
                  variant='outline'
                  className={`ml-2 ${getGradeDisplayClass(transcript.gpa)}`}
                >
                  {transcript.gpa.toFixed(2)}
                </Badge>
              </p>
              <p>
                <strong>Total Credits:</strong>{' '}
                {transcript.transcriptList.length * 3}
              </p>{' '}
              {/* Assuming each course is 3 credits */}
              <p>
                <strong>Courses Completed:</strong>{' '}
                {transcript.transcriptList.length}
              </p>
            </div>
          </div>
        </div>

        <Separator />

        <div>
          <h3 className='text-sm font-medium text-muted-foreground mb-3'>
            Course Details
          </h3>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Subject Code</TableHead>
                <TableHead>Subject Name</TableHead>
                <TableHead className='text-center'>Grade</TableHead>
                <TableHead className='text-center'>GPA</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {transcript.transcriptList.map(
                (course: Transcript, index: number) => (
                  <TableRow key={index}>
                    <TableCell className='font-medium'>
                      {course.subjectCode}
                    </TableCell>
                    <TableCell>{course.subjectName}</TableCell>
                    <TableCell className='text-center font-medium'>
                      {course.grade}
                    </TableCell>
                    <TableCell
                      className={`text-center font-medium ${getGradeDisplayClass(
                        course.gpa,
                      )}`}
                    >
                      {course.gpa.toFixed(2)}
                    </TableCell>
                  </TableRow>
                ),
              )}
            </TableBody>
          </Table>
        </div>
      </CardContent>

      <CardFooter className='text-sm text-muted-foreground border-t pt-4'>
        <div className='w-full flex justify-between items-center'>
          <span>Generated on {format(new Date(), 'PPP')}</span>
          <span>Student Management System</span>
        </div>
      </CardFooter>
    </Card>
  );
};

export default AcademicTranscript;
