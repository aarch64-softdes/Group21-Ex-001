import { Card, CardContent, CardHeader, CardTitle } from '@ui/card';
import { Separator } from '@ui/separator';
import { Badge } from '@ui/badge';
import { useSubject } from '@/features/subject/api/useSubjectApi';
import { DetailComponentProps } from '@/core/types/table';
import { Loader2, School, BookOpen, FileText } from 'lucide-react';
import React from 'react';
import { useTranslation } from 'react-i18next';

const SubjectDetail: React.FC<DetailComponentProps> = ({ id: subjectId }) => {
  const { t } = useTranslation(['subject', 'common']);
  const { data: subject, isLoading } = useSubject(subjectId as string);

  if (isLoading) {
    return (
      <div className='flex items-center justify-center h-48'>
        <Loader2 className='h-8 w-8 animate-spin text-primary' />
      </div>
    );
  }

  if (!subject) {
    return (
      <div className='flex items-center justify-center h-48'>
        <p className='text-muted-foreground'>{t('common:messages.noData')}</p>
      </div>
    );
  }

  return (
    <div className='container mx-auto p-6 max-w-4xl'>
      <Card className='mb-6'>
        <CardHeader>
          <div className='flex justify-between items-start'>
            <div>
              <CardTitle className='text-2xl font-bold'>
                {subject.name}
              </CardTitle>
              <p className='text-muted-foreground text-sm'>{subject.code}</p>
            </div>
            <Badge variant='secondary' className='px-3 py-1'>
              {subject.credits} {t('subject:fields.credits')}
            </Badge>
          </div>
          <Separator />
        </CardHeader>
        <CardContent>
          <div className='space-y-6'>
            <div>
              <h3 className='text-lg font-medium flex items-center gap-2 mb-2'>
                <FileText className='h-5 w-5' />
                {t('subject:sections.description')}
              </h3>
              <p className='text-muted-foreground'>
                {subject.description || t('common:messages.noData')}
              </p>
            </div>

            <div className='grid grid-cols-1 md:grid-cols-2 gap-6 pt-4'>
              {subject.faculty && (
                <div>
                  <h3 className='text-sm font-medium flex items-center gap-2 mb-1 text-muted-foreground'>
                    <School className='h-4 w-4' />
                    {t('subject:fields.faculty')}
                  </h3>
                  <p>{subject.faculty.name}</p>
                </div>
              )}

              <div>
                <h3 className='text-sm font-medium flex items-center gap-2 mb-1 text-muted-foreground'>
                  <BookOpen className='h-4 w-4' />
                  {t('subject:fields.prerequisites')}
                </h3>
                <div className='flex flex-wrap gap-2'>
                  {subject.prerequisites && subject.prerequisites.length > 0 ? (
                    subject.prerequisites.map((prerequisite, index) => (
                      <Badge key={index} variant='secondary'>
                        {prerequisite.name || 'Unknown'} -{' '}
                        {prerequisite.code || 'N/A'}
                      </Badge>
                    ))
                  ) : (
                    <p>{t('subject:messages.noPrerequisites')}</p>
                  )}
                </div>
              </div>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  );
};

export default SubjectDetail;
