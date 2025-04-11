import { Card, CardContent, CardHeader, CardTitle } from '@ui/card';
import { Separator } from '@ui/separator';
import { Badge } from '@ui/badge';
import { useClass } from '@/features/class/api/useClassApi';
import { DetailComponentProps } from '@/core/types/table';
import {
  Loader2,
  Calendar,
  BookOpen,
  Users,
  Clock,
  MapPin,
  User,
  GraduationCap,
} from 'lucide-react';
import React from 'react';

const ClassDetail: React.FC<DetailComponentProps> = ({ id: classId }) => {
  const { data: classData, isLoading } = useClass(classId as string);

  if (isLoading) {
    return (
      <div className='flex items-center justify-center h-48'>
        <Loader2 className='h-8 w-8 animate-spin text-primary' />
      </div>
    );
  }

  if (!classData) {
    return (
      <div className='flex items-center justify-center h-48'>
        <p className='text-muted-foreground'>Class not found</p>
      </div>
    );
  }

  // Helper to format date
  const formatDate = (date: Date) => {
    return new Date(date).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
    });
  };

  // Helper to interpret schedule
  const interpretSchedule = (schedule: string) => {
    const days = [
      'Monday',
      'Tuesday',
      'Wednesday',
      'Thursday',
      'Friday',
      'Saturday',
    ];

    try {
      // Parse schedule like "T2(3-6)"
      const dayMatch = schedule.match(/T([2-7])/);
      const periodMatch = schedule.match(/\((\d+)-(\d+)\)/);

      if (!dayMatch || !periodMatch) {
        return schedule;
      }

      const day = parseInt(dayMatch[1]) - 1; // T2 is Monday (index 0)
      const startPeriod = parseInt(periodMatch[1]);
      const endPeriod = parseInt(periodMatch[2]);

      return `${days[day]}, Period ${startPeriod}-${endPeriod}`;
    } catch (error) {
      return schedule;
    }
  };

  return (
    <div className='container mx-auto p-6 max-w-4xl'>
      <Card className='mb-6'>
        <CardHeader>
          <div className='flex justify-between items-start'>
            <div>
              <CardTitle className='text-2xl font-bold'>
                {classData.code}
              </CardTitle>
              <p className='text-muted-foreground text-sm'>
                {classData.subject?.name}
              </p>
            </div>
            <Badge variant='secondary' className='px-3 py-1'>
              {classData.year}
            </Badge>
          </div>
          <Separator />
        </CardHeader>
        <CardContent>
          <div className='grid grid-cols-1 md:grid-cols-2 gap-6 pt-4'>
            <div className='space-y-4'>
              <div>
                <h3 className='text-sm font-medium flex items-center gap-2 mb-1 text-muted-foreground'>
                  <BookOpen className='h-4 w-4' />
                  Subject
                </h3>
                <p>{classData.subject?.name}</p>
              </div>

              <div>
                <h3 className='text-sm font-medium flex items-center gap-2 mb-1 text-muted-foreground'>
                  <GraduationCap className='h-4 w-4' />
                  Program
                </h3>
                <p>{classData.program?.name}</p>
              </div>

              <div>
                <h3 className='text-sm font-medium flex items-center gap-2 mb-1 text-muted-foreground'>
                  <User className='h-4 w-4' />
                  Lecturer
                </h3>
                <p>{classData.lecturer}</p>
              </div>

              <div>
                <h3 className='text-sm font-medium flex items-center gap-2 mb-1 text-muted-foreground'>
                  <Users className='h-4 w-4' />
                  Maximum Students
                </h3>
                <p>{classData.maxStudent}</p>
              </div>

              <div>
                <h3 className='text-sm font-medium flex items-center gap-2 mb-1 text-muted-foreground'>
                  <GraduationCap className='h-4 w-4' />
                  Academic Year
                </h3>
                <p>{classData.year}</p>
              </div>
            </div>

            <div className='space-y-4'>
              <div>
                <h3 className='text-sm font-medium flex items-center gap-2 mb-1 text-muted-foreground'>
                  <Calendar className='h-4 w-4' />
                  Start Date
                </h3>
                <p>{formatDate(classData.startAt)}</p>
              </div>

              <div>
                <h3 className='text-sm font-medium flex items-center gap-2 mb-1 text-muted-foreground'>
                  <Clock className='h-4 w-4' />
                  Schedule
                </h3>
                <p>{interpretSchedule(classData.schedule)}</p>
                <p className='text-xs text-muted-foreground mt-1'>
                  Raw format: {classData.schedule}
                </p>
              </div>

              <div>
                <h3 className='text-sm font-medium flex items-center gap-2 mb-1 text-muted-foreground'>
                  <MapPin className='h-4 w-4' />
                  Room
                </h3>
                <p>{classData.room}</p>
              </div>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  );
};

export default ClassDetail;
