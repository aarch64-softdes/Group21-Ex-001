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
import { useCourse } from '@/features/course/api/useCourseApi';
import {
  useProgram,
  useProgramsDropdown,
} from '@/features/program/api/useProgramApi';
import {
  CreateCourseDTO,
  UpdateCourseDTO,
} from '@/features/course/types/course';
import { FormComponentPropsWithoutType } from '@/core/types/table';
import { Loader2 } from 'lucide-react';
import { useEffect } from 'react';
import LoadingButton from '@ui/loadingButton';
import {
  useSubject,
  useSubjectsDropdown,
} from '@/features/subject/api/useSubjectApi';
import LoadMoreSelect from '@/components/common/LoadMoreSelect';
import { parseSchedule } from '../types/courseSchedule';

// Schedule validation pattern - ex: T2(3-6)
const schedulePattern = /^T[2-7]\([1-9]-([1-9]|1[0-2])\)$/;

// Define schema
export const CourseFormSchema = z.object({
  subjectId: z.string().min(1, 'Subject is required'),
  programId: z.string().min(1, 'Program is required'),
  code: z.string().min(1, 'Code is required'),
  year: z
    .number({ invalid_type_error: 'Year must be a number' })
    .min(2020, 'Year must be 2020 or later')
    .max(2050, 'Year must be before 2050')
    .or(z.string().regex(/^\d+$/).transform(Number)),
  semester: z
    .number({ invalid_type_error: 'Semester must be a number' })
    .min(1, 'Semester must be at least 1')
    .max(3, 'Semester must be at most 3')
    .or(z.string().regex(/^\d+$/).transform(Number)),
  startDate: z.string().min(1, 'Start date is required'),
  lecturer: z
    .string()
    .min(1, 'Lecturer name is required')
    .max(100, 'Lecturer name must be less than 100 characters'),
  maxStudent: z
    .number({ invalid_type_error: 'Max students must be a number' })
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

export type CourseFormValues = z.infer<typeof CourseFormSchema>;

const CourseForm: React.FC<FormComponentPropsWithoutType> = ({
  onSubmit,
  onCancel,
  id,
  isLoading = false,
  isEditing = false,
}) => {
  const { data: courseData, isLoading: isLoadingCourse } = useCourse(id || '');
  const subjects = useSubjectsDropdown(isEditing ? 100 : 5, (subject) => ({
    id: subject.id,
    label: subject.name,
    value: subject.id,
    metadata: {
      isActive: subject.isActive,
    },
  }));
  const programs = useProgramsDropdown(isEditing ? 100 : 5, (program) => ({
    id: program.id,
    label: program.name,
    value: program.id,
  }));

  const form = useForm<CourseFormValues>({
    resolver: zodResolver(CourseFormSchema),
    defaultValues: {
      subjectId: '',
      programId: '',
      code: '',
      year: new Date().getFullYear(),
      semester: 1,
      startDate: '',
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

  // Reset form when course data is loaded
  useEffect(() => {
    if (courseData && id) {
      form.reset({
        subjectId: courseData.subject?.id,
        programId: courseData.program?.id,
        code: courseData.code,
        year: courseData.year,
        semester: courseData.semester,
        startDate: formatDateForInput(courseData.startDate),
        lecturer: courseData.lecturer,
        maxStudent: courseData.maxStudent,
        schedule: courseData.schedule,
        room: courseData.room,
      });
    }
  }, [courseData, id, form]);

  const handleSubmit = (values: CourseFormValues) => {
    // Transform the data for the backend
    const submissionValues = {
      ...values,
      subjectId: values.subjectId,
      programId: values.programId,
      startDate: new Date(values.startDate),
      schedule: parseSchedule(values.schedule),
    };

    if (isEditing) {
      const { subjectId, programId, ...updateData } = submissionValues;
      onSubmit(updateData as UpdateCourseDTO);
    } else {
      onSubmit(submissionValues as CreateCourseDTO);
    }
  };

  // Determine if we should show loading state
  const isFormLoading = isLoading || (isEditing && isLoadingCourse);

  return (
    <div className='sticky inset-0'>
      {/* Header */}
      <div className='border-b px-4 py-2 flex items-center justify-between'>
        <h2 className='text-xl font-semibold'>
          {isEditing ? 'Edit Course' : 'Add New Course'}
        </h2>
      </div>

      {/* Content */}
      <div className='flex-1 p-4 min-h-0'>
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
                      Course Information
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
                          <LoadMoreSelect
                            value={field.value}
                            onValueChange={field.onChange}
                            placeholder='Select subject'
                            items={subjects.selectItems}
                            isLoading={subjects.isLoading}
                            isLoadingMore={subjects.isLoadingMore}
                            hasMore={subjects.hasMore}
                            onLoadMore={subjects.loadMore}
                            emptyMessage='No subjects found.'
                            searchPlaceholder='Search subject...'
                            onSearch={subjects.setSubjectSearch}
                            disabled={isEditing || isLoading}
                            disabledItems={(metadata) =>
                              metadata.isActive === false
                            }
                          />
                          <FormMessage />
                        </FormItem>
                      )}
                    />

                    <FormField
                      control={form.control}
                      name='programId'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>Program</FormLabel>
                          <LoadMoreSelect
                            value={field.value}
                            onValueChange={field.onChange}
                            placeholder='Select program'
                            items={programs.selectItems}
                            isLoading={programs.isLoading}
                            isLoadingMore={programs.isLoadingMore}
                            hasMore={programs.hasMore}
                            onLoadMore={programs.loadMore}
                            emptyMessage='No programs found.'
                            searchPlaceholder='Search programs...'
                            onSearch={programs.setProgramSearch}
                            disabled={isEditing || isLoading}
                          />
                          <FormMessage />
                        </FormItem>
                      )}
                    />

                    <FormField
                      control={form.control}
                      name='code'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>Code</FormLabel>
                          <FormControl>
                            <Input
                              placeholder='e.g. CS101'
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
                      name='semester'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>Semester</FormLabel>
                          <FormControl>
                            <Input
                              type='number'
                              min={1}
                              max={3}
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
                      name='startDate'
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
                    {isEditing ? 'Save Changes' : 'Add Course'}
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

export default CourseForm;
