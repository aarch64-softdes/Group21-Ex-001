import React, { useState } from 'react';
import { useCourses } from '@/features/course/api/useCourseApi';
import { useEnrollCourse } from '../api/useEnrollmentApi';
import { Button } from '@ui/button';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@ui/dialog';
import { Loader2 } from 'lucide-react';
import GenericTable from '@/components/table/GenericTable';
import Course from '@/features/course/types/course';
import { Column } from '@/core/types/table';
import { useTranslation } from 'react-i18next';

interface AvailableCoursesTableProps {
  studentId: string;
}

interface CustomEnrollButtonProps {
  id: string;
  studentId: string;
  code: string;
}

const CustomEnrollButton: React.FC<CustomEnrollButtonProps> = ({
  id,
  studentId,
}) => {
  const { t } = useTranslation('enrollment');
  const [isEnrollDialogOpen, setIsEnrollDialogOpen] = useState(false);
  const enrollCourse = useEnrollCourse();

  const handleEnrollClick = () => {
    setIsEnrollDialogOpen(true);
  };

  const handleConfirmEnroll = async () => {
    await enrollCourse.mutateAsync({
      studentId,
      courseId: id,
    });
    setIsEnrollDialogOpen(false);
  };

  return (
    <>
      <Button onClick={handleEnrollClick}>
        {t('availableCourses.enroll')}
      </Button>

      <Dialog open={isEnrollDialogOpen} onOpenChange={setIsEnrollDialogOpen}>
        <DialogContent className='max-w-[425px]'>
          <DialogHeader>
            <DialogTitle>{t('availableCourses.confirmEnrollment')}</DialogTitle>
            <DialogDescription>
              {t('availableCourses.confirmEnrollmentMessage')}
            </DialogDescription>
          </DialogHeader>
          <DialogFooter>
            <Button
              variant='outline'
              onClick={() => setIsEnrollDialogOpen(false)}
              disabled={enrollCourse.isPending}
            >
              {t('common:actions.cancel')}
            </Button>
            <Button
              onClick={handleConfirmEnroll}
              disabled={enrollCourse.isPending}
            >
              {enrollCourse.isPending ? (
                <>
                  <Loader2 className='mr-2 h-4 w-4 animate-spin' />
                  {t('availableCourses.enrolling')}
                </>
              ) : (
                t('availableCourses.enroll')
              )}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </>
  );
};

const AvailableCoursesTable: React.FC<AvailableCoursesTableProps> = ({
  studentId,
}) => {
  const { t } = useTranslation('enrollment');

  const columns: Column<Course>[] = [
    {
      header: t('availableCourses.code'),
      key: 'code',
    },
    {
      header: t('availableCourses.subject'),
      key: 'subject.name',
      nested: true,
    },
    {
      header: t('availableCourses.schedule'),
      key: 'schedule',
    },
    {
      header: t('availableCourses.room'),
      key: 'room',
    },
    {
      header: t('availableCourses.semester'),
      key: 'semester',
      transform: (value, row) =>
        `${row?.year}, ${t('availableCourses.semester')} ${value}`,
    },
  ];

  return (
    <GenericTable
      columns={columns}
      addAction={{
        disabled: true,
        onAdd: () => {},
      }}
      queryHook={useCourses}
      filterOptions={[]}
      customActionCellComponent={CustomEnrollButton}
      metadata={studentId}
      emptyMessage={t('availableCourses.noCoursesFound')}
    />
  );
};

export default AvailableCoursesTable;
