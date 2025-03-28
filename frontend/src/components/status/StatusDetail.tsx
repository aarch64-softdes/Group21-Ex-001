import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Separator } from '@/components/ui/separator';
import { useStatus } from '@/hooks/api/useStatusApi';
import { DetailComponentProps } from '@/types/table';
import { Badge } from '@/components/ui/badge';
import { Loader2 } from 'lucide-react';
import React from 'react';

const StatusDetail: React.FC<DetailComponentProps> = ({ id: statusId }) => {
  const { data: status, isLoading } = useStatus(parseInt(statusId as string));

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
        <p className='text-muted-foreground'>Status not found</p>
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
                <p className='text-sm font-medium text-muted-foreground'>ID</p>
                <p>{status.id}</p>
              </div>
              <div>
                <p className='text-sm font-medium text-muted-foreground'>
                  Name
                </p>
                <p>{status.name}</p>
              </div>
            </div>
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle className='text-xl'>Allowed Transitions</CardTitle>
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
              <p className='text-muted-foreground'>No allowed transitions</p>
            )}
          </div>
        </CardContent>
      </Card>
    </div>
  );
};

export default StatusDetail;
