import { Card, CardContent, CardHeader, CardTitle } from '@ui/card';
import { Separator } from '@ui/separator';
import { useStatus } from '@/features/status/api/useStatusApi';
import { DetailComponentProps } from '@/core/types/table';
import { Badge } from '@ui/badge';
import { Loader2 } from 'lucide-react';
import React from 'react';
import { useTranslation } from 'react-i18next';

const StatusDetail: React.FC<DetailComponentProps> = ({ id: statusId }) => {
  const { t } = useTranslation(['status', 'common']);
  const { data: status, isLoading } = useStatus(statusId as string);

  if (isLoading) {
    return (
      <div className='flex items-center justify-center h-48'>
        <Loader2 className='h-8 w-8 animate-spin text-primary' />
      </div>
    );
  }

  if (!status) {
    return (
      <div className='flex items-center justify-center h-48'>
        <p className='text-muted-foreground'>{t('status:statusNotFound')}</p>
      </div>
    );
  }

  return (
    <div className='container mx-auto p-6 max-w-4xl'>
      <Card className='mb-6'>
        <CardHeader>
          <CardTitle className='text-xl'>{status.name}</CardTitle>
          <Separator />
        </CardHeader>
        <CardContent>
          <div className='mt-4'>
            <div className='grid grid-cols-2 gap-4'>
              <div>
                <p className='text-sm font-medium text-muted-foreground'>
                  {t('status:fields.id')}
                </p>
                <p>{status.id}</p>
              </div>
              <div>
                <p className='text-sm font-medium text-muted-foreground'>
                  {t('status:fields.name')}
                </p>
                <p>{status.name}</p>
              </div>
            </div>
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle className='text-xl'>
            {t('status:fields.allowedTransitions')}
          </CardTitle>
          <Separator />
        </CardHeader>
        <CardContent>
          <div className='mt-4'>
            {status.allowedTransitions &&
            status.allowedTransitions.length > 0 ? (
              <div className='flex flex-wrap gap-2'>
                {status.allowedTransitions.map((transition) => (
                  <Badge
                    key={transition.id}
                    variant='secondary'
                    className='px-3 py-1'
                  >
                    {transition.name}
                  </Badge>
                ))}
              </div>
            ) : (
              <p className='text-muted-foreground'>
                {t('status:noAllowedTransitions')}
              </p>
            )}
          </div>
        </CardContent>
      </Card>
    </div>
  );
};

export default StatusDetail;
