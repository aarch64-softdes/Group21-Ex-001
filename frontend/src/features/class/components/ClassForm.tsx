import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import * as z from 'zod';

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
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@ui/select';
import { useClass } from '@/features/class/api/useClassApi';
import { usePrograms } from '@/features/program/api/useProgramApi';
import Class, { CreateClassDto } from '@/features/class/types/class';
import { FormComponentProps } from '@/core/types/table';
import { Loader2 } from 'lucide-react';
import { useEffect } from 'react';
import LoadingButton from '@ui/loadingButton';
import { useSubjects } from '@/features/subject/api/useSubjectApi';

// Schedule validation pattern - ex: T2(3-6)
const schedulePattern = /^T[2-7]\([1-9]-([1-9]|1[0-2])\)$/;

// Define schema
export const ClassFormSchema = z.object({
  subjectId: z.string().min(1, 'Subject is required'),
  program: z.string().min(1, 'Program is required'),
  code: z
    .string()
    .min(1, 'Code is required')
    .max(20, 'Code must be less than 20 characters'),
  year: z
    .number()
    .min(2020, 'Year must be 2020 or later')
    .max(2050, 'Year must be before 2050')
    .or(z.string().regex(/^\d+$/).transform(Number)),
  startAt: z.string().min(1, 'Start date is required'),
  lecturer: z
    .string()
    .min(1, 'Lecturer name is required')
    .max(100, 'Lecturer name must be less than 100 characters'),
  maxStudent: z
    .number()
    .min(1, 'Max students must be at least 1')
    .max(200, 'Max students must be at most 200')
    .or(z.string().regex(/^\d+$/).transform(Number)),
  schedule: z
    .string()
    .min(1, 'Schedule is required')
    .regex(
      schedulePattern,
      'Schedule must be in format T2(3-6), day(start-end)',
    ),
  room: z
    .string()
    .min(1, 'Room is required')
    .max(50, 'Room must be less than 50 characters'),
});

export type ClassFormValues = z.infer<typeof ClassFormSchema>;

const ClassForm: React.FC<FormComponentProps<Class>> = ({
  onSubmit,
  onCancel,
  id,
  isLoading = false,
  isEditing = false,
}) => {
  const { data: classData, isLoading: isLoadingClass } = useClass(id || '');
  const { data: subjects, isLoading: isLoadingSubjects } = useSubjects({
    page: 1,
    pageSize: 100,
    filters: {},
    sort: { key: 'name', direction: 'asc' },
  });

  const { data: programsData, isLoading: isLoadingPrograms } = usePrograms({
    page: 1,
    pageSize: 100,
    filters: {},
    sort: { key: 'name', direction: 'asc' },
  });

  const form = useForm<ClassFormValues>({
    resolver: zodResolver(ClassFormSchema),
    defaultValues: {
      subjectId: '',
      program: '',
      code: '',
      year: new Date().getFullYear(),
      startAt: '',
      lecturer: '',
      maxStudent: 30,
      schedule: '',
      room: '',
    },
  });

  // Format date for input field
  const formatDateForInput = (date: Date | null): string => {
    if (!date) return '';
    return date.toISOString().split('T')[0];
  };

  // Reset form when class data is loaded
  useEffect(() => {
    if (classData && id) {
      form.reset({
        subjectId: classData.subjectId || '',
        program: classData.program || '',
        code: classData.code || '',
        year: classData.year || new Date().getFullYear(),
        startAt: formatDateForInput(classData.startAt),
        lecturer: classData.lecturer || '',
        maxStudent: classData.maxStudent || 30,
        schedule: classData.schedule || '',
        room: classData.room || '',
      });
    }
  }, [classData, id, form]);

  const handleSubmit = (values: ClassFormValues) => {
    // Transform the string date to a Date object before submitting
    const submissionValues = {
      ...values,
      startAt: new Date(values.startAt),
    };

    onSubmit(submissionValues as unknown as CreateClassDto);
  };

  // Determine if we should show loading state
  const isFormLoading = isLoading || (isEditing && isLoadingClass);

  return (
    <div className='sticky inset-0'>
      {/* Header */}
      <div className='border-b px-4 py-2 flex items-center justify-between'>
        <h2 className='text-xl font-semibold'>
          {isEditing ? 'Edit Class' : 'Add New Class'}
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
                      Class Information
                    </CardTitle>
                    <Separator />
                  </CardHeader>
                  <CardContent className='grid grid-cols-1 md:grid-cols-2 gap-6'>
                    <FormField
                      control={form.control}
                      name='subjectId'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>Subject</FormLabel>
                          <Select
                            onValueChange={field.onChange}
                            value={field.value}
                            defaultValue={field.value}
                          >
                            <FormControl>
                              <SelectTrigger>
                                <SelectValue placeholder='Select subject' />
                              </SelectTrigger>
                            </FormControl>
                            <SelectContent>
                              {isLoadingSubjects ? (
                                <div className='flex items-center justify-center p-2'>
                                  <Loader2 className='h-4 w-4 animate-spin' />
                                </div>
                              ) : subjects?.data && subjects.data.length > 0 ? (
                                subjects.data.map((subject) => (
                                  <SelectItem
                                    key={subject.id}
                                    value={subject.id}
                                  >
                                    {subject.name}
                                  </SelectItem>
                                ))
                              ) : (
                                <SelectItem value='' disabled>
                                  No subjects available
                                </SelectItem>
                              )}
                            </SelectContent>
                          </Select>
                          <FormMessage />
                        </FormItem>
                      )}
                    />

                    <FormField
                      control={form.control}
                      name='program'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>Program</FormLabel>
                          <FormControl>
                            <Select
                              onValueChange={field.onChange}
                              value={field.value || undefined}
                            >
                              <FormControl>
                                <SelectTrigger className=''>
                                  <SelectValue placeholder='Select program' />
                                </SelectTrigger>
                              </FormControl>
                              <SelectContent>
                                {isLoadingPrograms ? (
                                  <div className='flex items-center justify-center p-2'>
                                    <Loader2 className='h-4 w-4 animate-spin' />
                                  </div>
                                ) : programsData?.data ? (
                                  programsData.data.map((program) => (
                                    <SelectItem
                                      key={program.id}
                                      value={program.name}
                                    >
                                      {program.name}
                                    </SelectItem>
                                  ))
                                ) : (
                                  <SelectItem value='' disabled>
                                    Failed to load programs
                                  </SelectItem>
                                )}
                              </SelectContent>
                            </Select>
                          </FormControl>
                          <FormMessage />
                        </FormItem>
                      )}
                    />

                    <FormField
                      control={form.control}
                      name='code'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>Class Code</FormLabel>
                          <FormControl>
                            <Input
                              placeholder='e.g. CS101-01'
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

                    <FormField
                      control={form.control}
                      name='year'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>Academic Year</FormLabel>
                          <FormControl>
                            <Input
                              type='number'
                              min={2020}
                              max={2050}
                              {...field}
                              onChange={(e) =>
                                field.onChange(parseInt(e.target.value, 10))
                              }
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

                    <FormField
                      control={form.control}
                      name='startAt'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>Start Date</FormLabel>
                          <FormControl>
                            <Input
                              type='date'
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

                    <FormField
                      control={form.control}
                      name='lecturer'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>Lecturer</FormLabel>
                          <FormControl>
                            <Input
                              placeholder='e.g. Dr. John Smith'
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

                    <FormField
                      control={form.control}
                      name='maxStudent'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>Maximum Students</FormLabel>
                          <FormControl>
                            <Input
                              type='number'
                              min={1}
                              max={200}
                              {...field}
                              onChange={(e) =>
                                field.onChange(parseInt(e.target.value, 10))
                              }
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

                    <FormField
                      control={form.control}
                      name='schedule'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>Schedule</FormLabel>
                          <FormControl>
                            <Input
                              placeholder='e.g. T2(3-6)'
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
                          <p className='text-xs text-muted-foreground'>
                            Format: T2(3-6) means Monday from 3rd to 6th period
                          </p>
                        </FormItem>
                      )}
                    />

                    <FormField
                      control={form.control}
                      name='room'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>Room</FormLabel>
                          <FormControl>
                            <Input
                              placeholder='e.g. A101'
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
                    {isEditing ? 'Save Changes' : 'Add Class'}
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

export default ClassForm;
