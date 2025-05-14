import { Card, CardContent, CardHeader, CardTitle } from '@ui/card';
import { Separator } from '@ui/separator';
import { Badge } from '@ui/badge';
import { useCourse } from '@/features/course/api/useCourseApi';
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
  CalendarRange,
  CornerUpRight,
} from 'lucide-react';
import React from 'react';
import { Button } from '@/components/ui/button';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

const CourseDetail: React.FC<DetailComponentProps> = ({ id: courseId }) => {
  const navigate = useNavigate();
  const { t } = useTranslation(['course', 'common']);

  const { data: courseData, isLoading } = useCourse(courseId as string);

  if (isLoading) {
    return (
      <div className='flex items-center justify-center h-48'>
        <Loader2 className='h-8 w-8 animate-spin text-primary' />
      </div>
    );
  }

  if (!courseData) {
    return (
      <div className='flex items-center justify-center h-48'>
        <p className='text-muted-foreground'>{t('common:messages.noData')}</p>
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
      t('course:days.monday'),
      t('course:days.tuesday'),
      t('course:days.wednesday'),
      t('course:days.thursday'),
      t('course:days.friday'),
      t('course:days.saturday'),
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

      return `${days[day]}, ${t(
        'course:fields.schedule',
      )} ${startPeriod}-${endPeriod}`;
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
                {courseData.code ||
                  `${t('course:fields.code')} #${courseData.id}`}
              </CardTitle>
              <p className='text-muted-foreground text-sm'>
                {courseData.subject?.name}
              </p>
            </div>
            <div className='flex flex-col gap-3 items-end'>
              <Badge variant='secondary' className='px-3 py-1'>
                {courseData.year} - {t('course:fields.semester')}{' '}
                {courseData.semester}
              </Badge>
              <Button
                variant='outline'
                className='mt-2 md:mt-0'
                onClick={() => navigate(`/course/${courseId}/enrollments`)}
              >
                {t('enrollment:title')} {courseData.code}
                <CornerUpRight className='ml-2 h-4 w-4' />
              </Button>
            </div>
          </div>
          <Separator />
        </CardHeader>
        <CardContent>
          <div className='grid grid-cols-1 md:grid-cols-2 gap-6 pt-4'>
            <div className='space-y-4'>
              <div>
                <h3 className='text-sm font-medium flex items-center gap-2 mb-1 text-muted-foreground'>
                  <BookOpen className='h-4 w-4' />
                  {t('course:fields.subject')}
                </h3>
                <p>{courseData.subject?.name}</p>
                <p className='text-xs text-muted-foreground mt-1'>
                  {t('course:fields.code')}: {courseData.subject?.code}
                </p>
              </div>

              <div>
                <h3 className='text-sm font-medium flex items-center gap-2 mb-1 text-muted-foreground'>
                  <GraduationCap className='h-4 w-4' />
                  {t('course:fields.program')}
                </h3>
                <p>{courseData.program?.name}</p>
              </div>

              <div>
                <h3 className='text-sm font-medium flex items-center gap-2 mb-1 text-muted-foreground'>
                  <User className='h-4 w-4' />
                  {t('course:fields.lecturer')}
                </h3>
                <p>{courseData.lecturer}</p>
              </div>

              <div>
                <h3 className='text-sm font-medium flex items-center gap-2 mb-1 text-muted-foreground'>
                  <Users className='h-4 w-4' />
                  {t('course:fields.maxStudent')}
                </h3>
                <p>{courseData.maxStudent}</p>
              </div>

              <div>
                <h3 className='text-sm font-medium flex items-center gap-2 mb-1 text-muted-foreground'>
                  <CalendarRange className='h-4 w-4' />
                  {t('course:sections.scheduleInfo')}
                </h3>
                <p>
                  {t('course:fields.year')} {courseData.year},{' '}
                  {t('course:fields.semester')} {courseData.semester}
                </p>
              </div>
            </div>

            <div className='space-y-4'>
              <div>
                <h3 className='text-sm font-medium flex items-center gap-2 mb-1 text-muted-foreground'>
                  <Calendar className='h-4 w-4' />
                  {t('course:fields.startDate')}
                </h3>
                <p>{formatDate(courseData.startDate)}</p>
              </div>

              <div>
                <h3 className='text-sm font-medium flex items-center gap-2 mb-1 text-muted-foreground'>
                  <Clock className='h-4 w-4' />
                  {t('course:fields.schedule')}
                </h3>
                <p>{interpretSchedule(courseData.schedule)}</p>
                <p className='text-xs text-muted-foreground mt-1'>
                  {courseData.schedule}
                </p>
              </div>

              <div>
                <h3 className='text-sm font-medium flex items-center gap-2 mb-1 text-muted-foreground'>
                  <MapPin className='h-4 w-4' />
                  {t('course:fields.room')}
                </h3>
                <p>{courseData.room}</p>
              </div>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  );
};

export default CourseDetail;
