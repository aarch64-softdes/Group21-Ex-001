import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Separator } from '@/components/ui/separator';
import { useFaculty } from '@/hooks/api/useFacultyApi';
import { DetailComponentProps } from '@/types/table';
import { Loader2 } from 'lucide-react';
import React from 'react';

const FacultyDetail: React.FC<DetailComponentProps> = ({ id: facultyId }) => {
  const { data: faculty, isLoading } = useFaculty(
    facultyId ? parseInt(facultyId as string, 10) : 0,
  );

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
        <p className='text-muted-foreground'>Faculty not found</p>
      </div>
    );
  }

  return (
    <div className='container mx-auto p-6 max-w-4xl'>
      <Card>
        <CardHeader>
          <CardTitle className='text-xl'>
            <p className='inline italic text-muted-foreground mr-2'>Faculty:</p>
            {faculty.name}
          </CardTitle>
          <Separator />
        </CardHeader>
        <CardContent>
          <div className='mt-4'>
            <div className='grid grid-cols-2 gap-4'>
              <div>
                <p className='text-sm font-medium text-muted-foreground'>ID:</p>
                <p>{faculty.id}</p>
              </div>
              <div>
                <p className='text-sm font-medium text-muted-foreground'>
                  Name:
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
