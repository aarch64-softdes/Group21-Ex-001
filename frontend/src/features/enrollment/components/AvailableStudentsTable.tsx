import React, { useState } from 'react';
import { useStudents } from '@/features/student/api/useStudentApi';
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
import { Loader2, Search } from 'lucide-react';
import GenericTable from '@/components/table/GenericTable';
import { Column } from '@/core/types/table';
import { Badge } from '@ui/badge';
import { SearchFilterOption } from '@/core/types/filter';
import Student from '@/features/student/types/student';
import { useTranslation } from 'react-i18next';

interface AvailableStudentsTableProps {
  courseId: string;
}

interface CustomEnrollButtonProps extends Student {
  courseId: string;
}

const CustomEnrollButton: React.FC<CustomEnrollButtonProps> = ({
  id,
  courseId,
}) => {
  const { t } = useTranslation('enrollment');
  const [isEnrollDialogOpen, setIsEnrollDialogOpen] = useState(false);
  const enrollCourse = useEnrollCourse();

  const handleEnrollClick = () => {
    setIsEnrollDialogOpen(true);
  };

  const handleConfirmEnroll = async () => {
    await enrollCourse.mutateAsync({
      studentId: id,
      courseId,
    });
    setIsEnrollDialogOpen(false);
  };

  return (
    <>
      <Button size='sm' onClick={handleEnrollClick}>
        {t('availableStudents.enroll')}
      </Button>

      <Dialog open={isEnrollDialogOpen} onOpenChange={setIsEnrollDialogOpen}>
        <DialogContent className='max-w-[425px]'>
          <DialogHeader>
            <DialogTitle>
              {t('availableStudents.confirmEnrollment')}
            </DialogTitle>
            <DialogDescription>
              {t('availableStudents.confirmEnrollmentMessage')}
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
                  {t('availableStudents.enrolling')}
                </>
              ) : (
                t('availableStudents.enroll')
              )}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </>
  );
};

const AvailableStudentsTable: React.FC<AvailableStudentsTableProps> = ({
  courseId,
}) => {
  const { t } = useTranslation('enrollment');

  const columns: Column<Student>[] = [
    {
      header: t('availableStudents.studentId'),
      key: 'id',
    },
    {
      header: t('availableStudents.name'),
      key: 'name',
    },
    {
      header: t('availableStudents.faculty'),
      key: 'faculty',
    },
    {
      header: t('availableStudents.program'),
      key: 'program',
    },
    {
      header: t('availableStudents.status'),
      key: 'status',
      transform: (value) => (
        <Badge variant='outline' className='text-xs'>
          {value}
        </Badge>
      ),
    },
  ];

  const searchNameFilterOption: SearchFilterOption = {
    id: 'search',
    label: t('common:actions.search'),
    labelIcon: Search,
    placeholder: t('availableStudents.searchPlaceholder'),
    type: 'search',
  };

  return (
    <div className='bg-white rounded-md p-4'>
      <GenericTable
        columns={columns}
        addAction={{
          disabled: true,
          onAdd: () => {},
        }}
        queryHook={useStudents}
        filterOptions={[searchNameFilterOption]}
        customActionCellComponent={CustomEnrollButton}
        metadata={{ courseId }}
        emptyMessage={t('availableStudents.noStudentsFound')}
      />
    </div>
  );
};

export default AvailableStudentsTable;
