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
import { useProgram } from '@/hooks/api/useProgramApi';
import Program, { CreateProgramDTO } from '@/types/program';
import { FormComponentProps } from '@/types/table';
import { Loader2 } from 'lucide-react';
import { useEffect } from 'react';
import LoadingButton from '../ui/loadingButton';

// Define schema
export const ProgramFormSchema = z.object({
  name: z
    .string()
    .min(1, 'Name is required')
    .max(255, 'Name must be less than 255 characters')
    .regex(/^[\p{L}\s]*$/u, 'Name must contain only letters and spaces'),
});

export type ProgramFormValues = z.infer<typeof ProgramFormSchema>;

const ProgramForm: React.FC<FormComponentProps<Program>> = ({
  onSubmit,
  onCancel,
  id,
  isLoading = false,
  isEditing = false,
}) => {
  const { data: programData, isLoading: isLoadingProgram } = useProgram(
    id ? parseInt(id, 10) : 0,
  );

  const form = useForm<ProgramFormValues>({
    resolver: zodResolver(ProgramFormSchema),
    defaultValues: {
      name: '',
    },
  });

  // Reset form when program data is loaded
  useEffect(() => {
    if (programData && id) {
      form.reset({
        name: programData.name || '',
      });
    }
  }, [programData, id, form]);

  const handleSubmit = (values: ProgramFormValues) => {
    onSubmit(values as unknown as CreateProgramDTO);
  };

  // Determine if we should show loading state
  const isFormLoading = isLoading || (isEditing && isLoadingProgram);

  return (
    <div className='sticky inset-0'>
      {/* Header */}
      <div className='border-b px-4 py-2 flex items-center justify-between'>
        <h2 className='text-xl font-semibold'>
          {isEditing ? 'Edit Program' : 'Add New Program'}
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
                      Program Information
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
                              placeholder='Enter program name'
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
                    {isEditing ? 'Save Changes' : 'Add Program'}
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

export default ProgramForm;
