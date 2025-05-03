import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import * as z from 'zod';
import { useTranslation } from 'react-i18next';

import { Button } from '@ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@ui/card';
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@ui/form';
import { Input } from '@ui/input';
import { Separator } from '@ui/separator';
import { useFaculty } from '@faculty/api/useFacultyApi';
import Faculty, { CreateFacultyDTO } from '@faculty/types/faculty';
import { FormComponentProps } from '@/core/types/table';
import { Loader2 } from 'lucide-react';
import { useEffect } from 'react';
import LoadingButton from '@ui/loadingButton';

// Define schema
const FacultyFormSchema = (t: any) =>
  z.object({
    name: z
      .string()
      .min(1, t('validation.required'))
      .max(255, t('validation.maxLength', { length: 255 }))
      .regex(/^[\p{L}\s]*$/u, t('validation.nameFormat')),
  });

export type FacultyFormValues = z.infer<ReturnType<typeof FacultyFormSchema>>;

const FacultyForm: React.FC<FormComponentProps<Faculty>> = ({
  onSubmit,
  onCancel,
  id,
  isLoading = false,
  isEditing = false,
}) => {
  const { t } = useTranslation(['faculty', 'common']);

  const { data: facultyData, isLoading: isLoadingFaculty } = useFaculty(
    id || '',
  );

  const form = useForm<FacultyFormValues>({
    resolver: zodResolver(FacultyFormSchema(t)),
    defaultValues: {
      name: '',
    },
  });

  // Reset form when faculty data is loaded
  useEffect(() => {
    if (facultyData && id) {
      form.reset({
        name: facultyData.name || '',
      });
    }
  }, [facultyData, id, form]);

  const handleSubmit = (values: FacultyFormValues) => {
    onSubmit(values as unknown as CreateFacultyDTO);
  };

  // Determine if we should show loading state
  const isFormLoading = isLoading || (isEditing && isLoadingFaculty);

  return (
    <div className='sticky inset-0'>
      {/* Header */}
      <div className='border-b px-4 py-2 flex items-center justify-between'>
        <h2 className='text-xl font-semibold'>
          {isEditing ? t('faculty:editFaculty') : t('faculty:addNew')}
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
                      {t('faculty:sections.facultyInfo')}
                    </CardTitle>
                    <Separator />
                  </CardHeader>
                  <CardContent>
                    <FormField
                      control={form.control}
                      name='name'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>{t('faculty:fields.name')}</FormLabel>
                          <FormControl>
                            <Input
                              placeholder={t('faculty:fields.namePlaceholder')}
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
                    {t('common:actions.cancel')}
                  </Button>
                  <LoadingButton type='submit' isLoading={isLoading}>
                    {isEditing
                      ? t('common:actions.save')
                      : t('common:actions.add')}
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

export default FacultyForm;
