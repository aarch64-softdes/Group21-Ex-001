import React, { useRef, useState } from 'react';
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
import StudentService from '@/features/student/api/studentService';
import '../styles/transcript-print.css';
import { useTranslation } from 'react-i18next';

interface AcademicTranscriptProps {
  studentId: string;
}

const studentService = new StudentService();

const AcademicTranscript: React.FC<AcademicTranscriptProps> = ({
  studentId,
}) => {
  const { t } = useTranslation('enrollment');
  const transcriptRef = useRef<HTMLDivElement>(null);
  const [isExporting, setIsExporting] = useState(false);
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

  const handleDownload = async () => {
    try {
      setIsExporting(true);

      // Get the blob data from the API
      const blob = await studentService.exportTranscript(studentId);

      // Create a download link
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;

      // Set filename with student name if available
      const filename = transcript
        ? `transcript_${transcript.studentName.replace(/\s+/g, '_')}.pdf`
        : `transcript_${studentId}.pdf`;

      link.setAttribute('download', filename);

      // Append to the document, click it, and clean up
      document.body.appendChild(link);
      link.click();

      // Clean up
      window.URL.revokeObjectURL(url);
      document.body.removeChild(link);
    } catch (error) {
      console.error('Error downloading transcript:', error);
      alert('Failed to download transcript. Please try again later.');
    } finally {
      setIsExporting(false);
    }
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
        <p className='text-red-500'>{t('transcript.noTranscriptData')}</p>
      </div>
    );
  }

  if (!transcript) {
    return (
      <div className='bg-white rounded-md p-6 text-center'>
        <p className='text-muted-foreground'>
          {t('transcript.noTranscriptData')}
        </p>
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
            <CardTitle className='text-2xl'>{t('transcript.title')}</CardTitle>
            <CardDescription>{t('transcript.subtitle')}</CardDescription>
          </div>
          <div className='flex space-x-2 print-hidden'>
            <Button variant='outline' size='sm' onClick={handlePrint}>
              <Printer className='h-4 w-4 mr-2' />
              {t('transcript.print')}
            </Button>
            <Button
              variant='outline'
              size='sm'
              onClick={handleDownload}
              disabled={isExporting}
            >
              {isExporting ? (
                <>
                  <Loader2 className='h-4 w-4 mr-2 animate-spin' />
                  {t('transcript.exporting')}
                </>
              ) : (
                <>
                  <Download className='h-4 w-4 mr-2' />
                  {t('transcript.download')}
                </>
              )}
            </Button>
          </div>
        </div>
      </CardHeader>

      <CardContent className='space-y-6'>
        <div className='grid grid-cols-2 gap-4'>
          <div>
            <h3 className='text-sm font-medium text-muted-foreground'>
              {t('transcript.studentInfo')}
            </h3>
            <div className='mt-2 space-y-1'>
              <p>
                <strong>{t('transcript.name')}:</strong>{' '}
                {transcript.studentName}
              </p>
              <p>
                <strong>{t('transcript.id')}:</strong> {transcript.studentId}
              </p>
              <p>
                <strong>{t('transcript.dob')}:</strong>{' '}
                {transcript.studentDob
                  ? format(new Date(transcript.studentDob), 'PPP')
                  : 'N/A'}
              </p>
              <p>
                <strong>{t('transcript.program')}:</strong>{' '}
                {transcript.courseName || 'N/A'}
              </p>
            </div>
          </div>

          <div>
            <h3 className='text-sm font-medium text-muted-foreground'>
              {t('transcript.academicSummary')}
            </h3>
            <div className='mt-2 space-y-1'>
              <p className='flex items-center justify-between'>
                <span>
                  <strong>{t('transcript.cumulativeGPA')}:</strong>
                </span>
                <Badge
                  variant='outline'
                  className={`ml-2 ${getGradeDisplayClass(transcript.gpa)}`}
                >
                  {transcript.gpa.toFixed(2)}
                </Badge>
              </p>
              <p>
                <strong>{t('transcript.totalCredits')}:</strong>{' '}
                {transcript.transcriptList.length * 3}
              </p>
              <p>
                <strong>{t('transcript.coursesCompleted')}:</strong>{' '}
                {transcript.transcriptList.length}
              </p>
            </div>
          </div>
        </div>

        <Separator />

        <div>
          <h3 className='text-sm font-medium text-muted-foreground mb-3'>
            {t('transcript.courseDetails')}
          </h3>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>{t('transcript.subjectCode')}</TableHead>
                <TableHead>{t('transcript.subjectName')}</TableHead>
                <TableHead className='text-center'>
                  {t('transcript.grade')}
                </TableHead>
                <TableHead className='text-center'>
                  {t('transcript.gpa')}
                </TableHead>
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
          <span>
            {t('transcript.generatedOn')} {format(new Date(), 'PPP')}
          </span>
          <span>{t('transcript.system')}</span>
        </div>
      </CardFooter>
    </Card>
  );
};

export default AcademicTranscript;
