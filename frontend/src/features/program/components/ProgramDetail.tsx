import { Card, CardContent, CardHeader, CardTitle } from '@ui/card';
import { Separator } from '@ui/separator';
import { useProgram } from '@/features/program/api/useProgramApi';
import { DetailComponentProps } from '@/core/types/table';
import { Loader2 } from 'lucide-react';
import React from 'react';
import { useTranslation } from 'react-i18next';

const ProgramDetail: React.FC<DetailComponentProps> = ({ id: programId }) => {
  const { t } = useTranslation(['program', 'common']);
  const { data: program, isLoading } = useProgram(programId as string);

  if (isLoading) {
    return (
      <div className='flex items-center justify-center h-48'>
        <Loader2 className='h-8 w-8 animate-spin text-primary' />
      </div>
    );
  }

  if (!program) {
    return (
      <div className='flex items-center justify-center h-48'>
        <p className='text-muted-foreground'>{t('program:programNotFound')}</p>
      </div>
    );
  }

  return (
    <div className='container mx-auto p-6 max-w-4xl'>
      <Card>
        <CardHeader>
          <CardTitle className='text-xl'>{program.name}</CardTitle>
          <Separator />
        </CardHeader>
        <CardContent>
          <div className='mt-4'>
            <div className='grid grid-cols-2 gap-4'>
              <div>
                <p className='text-sm font-medium text-muted-foreground'>
                  {t('program:fields.id')}
                </p>
                <p>{program.id}</p>
              </div>
              <div>
                <p className='text-sm font-medium text-muted-foreground'>
                  {t('program:fields.name')}
                </p>
                <p>{program.name}</p>
              </div>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  );
};

export default ProgramDetail;
