import { Card, CardContent, CardHeader, CardTitle } from '@ui/card';
import { Separator } from '@ui/separator';
import { useFaculty } from '@/features/faculty/api/useFacultyApi';
import { DetailComponentProps } from '@/core/types/table';
import { Loader2 } from 'lucide-react';
import React from 'react';
import { useTranslation } from 'react-i18next';

const FacultyDetail: React.FC<DetailComponentProps> = ({ id: facultyId }) => {
  const { t } = useTranslation(['faculty', 'common']);
  const { data: faculty, isLoading } = useFaculty(facultyId as string);

  if (isLoading) {
    return (
      <div className='flex items-center justify-center h-48'>
        <Loader2 className='h-8 w-8 animate-spin text-primary' />
      </div>
    );
  }

  if (!faculty) {
    return (
      <div className='flex items-center justify-center h-48'>
        <p className='text-muted-foreground'>{t('faculty:facultyNotFound')}</p>
      </div>
    );
  }

  return (
    <div className='container mx-auto p-6 max-w-4xl'>
      <Card>
        <CardHeader>
          <CardTitle className='text-xl'>
            <p className='inline italic text-muted-foreground mr-2'>
              {t('faculty:fields.faculty')}:
            </p>
            {faculty.name}
          </CardTitle>
          <Separator />
        </CardHeader>
        <CardContent>
          <div className='mt-4'>
            <div className='grid grid-cols-2 gap-4'>
              <div>
                <p className='text-sm font-medium text-muted-foreground'>
                  {t('faculty:fields.id')}:
                </p>
                <p>{faculty.id}</p>
              </div>
              <div>
                <p className='text-sm font-medium text-muted-foreground'>
                  {t('faculty:fields.name')}:
                </p>
                <p>{faculty.name}</p>
              </div>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  );
};

export default FacultyDetail;
