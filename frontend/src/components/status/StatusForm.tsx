import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import * as z from 'zod';

import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form';
import { Input } from '@/components/ui/input';
import { Separator } from '@/components/ui/separator';
import { useStatus } from '@/hooks/api/useStatusApi';
import Status, { CreateStatusDTO } from '@/types/status';
import { FormComponentProps } from '@/types/table';
import { Loader2 } from 'lucide-react';
import { useEffect } from 'react';
import LoadingButton from '../ui/loadingButton';

// Define schema
export const StatusFormSchema = z.object({
  name: z
    .string()
    .min(1, 'Name is required')
    .max(255, 'Name must be less than 255 characters')
    .regex(/^[\p{L}\s]*$/u, 'Name must contain only letters and spaces'),
});

export type StatusFormValues = z.infer<typeof StatusFormSchema>;

const StatusForm: React.FC<FormComponentProps<Status>> = ({
  onSubmit,
  onCancel,
  id,
  isLoading = false,
  isEditing = false,
}) => {
  const { data: statusData, isLoading: isLoadingStatus } = useStatus(
    id ? parseInt(id, 10) : 0,
  );

  const form = useForm<StatusFormValues>({
    resolver: zodResolver(StatusFormSchema),
    defaultValues: {
      name: '',
    },
  });

  // Reset form when status data is loaded
  useEffect(() => {
    if (statusData && id) {
      form.reset({
        name: statusData.name || '',
      });
    }
  }, [statusData, id, form]);

  const handleSubmit = (values: StatusFormValues) => {
    onSubmit(values as unknown as CreateStatusDTO);
  };

  // Determine if we should show loading state
  const isFormLoading = isLoading || (isEditing && isLoadingStatus);

  return (
    <div className='sticky inset-0'>
      {/* Header */}
      <div className='border-b px-4 py-2 flex items-center justify-between'>
        <h2 className='text-xl font-semibold'>
          {isEditing ? 'Edit Status' : 'Add New Status'}
        </h2>
      </div>

      {/* Content */}
      <div className='flex-1 overflow-y-auto p-4 min-h-0'>
        <div className='max-w-5xl mx-auto pb-4'>
          <Form {...form}>
            {isFormLoading ? (
              <div className='flex items-center justify-center p-6'>
                <Loader2 className='h-8 w-8 animate-spin' />
              </div>
            ) : (
              <form
                onSubmit={form.handleSubmit(handleSubmit)}
                className='max-w-5xl mx-auto'
                autoComplete='off'
              >
                <Card className='mb-6'>
                  <CardHeader>
                    <CardTitle className='text-lg font-medium'>
                      Status Information
                    </CardTitle>
                    <Separator />
                  </CardHeader>
                  <CardContent>
                    <FormField
                      control={form.control}
                      name='name'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>Name</FormLabel>
                          <FormControl>
                            <Input
                              placeholder='Enter status name'
                              {...field}
                              autoComplete='off'
                              onKeyDown={(e) => {
                                if (e.key === 'Enter') {
                                  e.preventDefault();
                                }
                              }}
                            />
                          </FormControl>
                          <FormMessage />
                        </FormItem>
                      )}
                    />
                  </CardContent>
                </Card>

                <div className='flex justify-end gap-2 pt-4'>
                  <Button
                    type='button'
                    variant='outline'
                    onClick={() => {
                      form.reset();
                      onCancel();
                    }}
                  >
                    Cancel
                  </Button>
                  <LoadingButton type='submit' isLoading={isLoading}>
                    {isEditing ? 'Save Changes' : 'Add Status'}
                  </LoadingButton>
                </div>
              </form>
            )}
          </Form>
        </div>
      </div>
    </div>
  );
};

export default StatusForm;
