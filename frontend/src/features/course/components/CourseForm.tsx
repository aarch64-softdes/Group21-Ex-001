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
import { useCourse } from '@/features/course/api/useCourseApi';
import { useProgramsDropdown } from '@/features/program/api/useProgramApi';
import {
  CreateCourseDTO,
  UpdateCourseDTO,
} from '@/features/course/types/course';
import { FormComponentPropsWithoutType } from '@/core/types/table';
import { Loader2 } from 'lucide-react';
import { useEffect } from 'react';
import LoadingButton from '@ui/loadingButton';
import { useSubjectsDropdown } from '@/features/subject/api/useSubjectApi';
import LoadMoreSelect from '@/components/common/LoadMoreSelect';
import { parseSchedule } from '../types/courseSchedule';

// Schedule validation pattern - ex: T2(3-6)
const schedulePattern =
  /^(T[2-7]|CN|Mon|Tue|Wed|Thu|Fri|Sat)\([1-9]-([1-9]|1[0-2])\)$/;

// Define schema
export const CourseFormSchema = (t: any) =>
  z.object({
    subjectId: z.string().min(1, t('course:validation.subjectRequired')),
    programId: z.string().min(1, t('course:validation.programRequired')),
    code: z.string().min(1, t('course:validation.codeRequired')),
    year: z
      .number({ invalid_type_error: t('course:validation.yearRequired') })
      .min(2020, t('course:validation.yearRequired'))
      .max(2050, t('course:validation.yearMax'))
      .or(z.string().regex(/^\d+$/).transform(Number)),
    semester: z
      .number({ invalid_type_error: t('course:validation.semesterMin') })
      .min(1, t('course:validation.semesterMin'))
      .max(3, t('course:validation.semesterMax'))
      .or(z.string().regex(/^\d+$/).transform(Number)),
    startDate: z.string().min(1, t('course:validation.startDateRequired')),
    lecturer: z
      .string()
      .min(1, t('course:validation.lecturerRequired'))
      .max(100, t('course:validation.lecturerMax')),
    maxStudent: z
      .number({ invalid_type_error: t('course:validation.maxStudentInvalid') })
      .min(1, t('course:validation.maxStudentMin'))
      .max(200, t('course:validation.maxStudentMax'))
      .or(z.string().regex(/^\d+$/).transform(Number)),
    schedule: z
      .string()
      .min(1, t('course:validation.scheduleRequired'))
      .regex(schedulePattern, t('course:validation.scheduleFormat')),
    room: z
      .string()
      .min(1, t('course:validation.roomRequired'))
      .max(50, t('course:validation.roomMax')),
  });

export type CourseFormValues = z.infer<ReturnType<typeof CourseFormSchema>>;

const CourseForm: React.FC<FormComponentPropsWithoutType> = ({
  onSubmit,
  onCancel,
  id,
  isLoading = false,
  isEditing = false,
}) => {
  const { t } = useTranslation(['course', 'common']);

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
    resolver: zodResolver(CourseFormSchema(t)),
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
      const { subjectId, programId, code, ...updateData } = submissionValues;
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
          {isEditing ? t('course:editCourse') : t('course:addNew')}
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
                      {t('course:sections.courseInfo')}
                    </CardTitle>
                    <Separator />
                  </CardHeader>
                  <CardContent className='grid grid-cols-1 md:grid-cols-2 gap-6'>
                    <FormField
                      control={form.control}
                      name='subjectId'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>{t('course:fields.subject')}</FormLabel>
                          <LoadMoreSelect
                            value={field.value}
                            onValueChange={field.onChange}
                            placeholder={t('course:fields.selectSubject')}
                            items={subjects.selectItems}
                            isLoading={subjects.isLoading}
                            isLoadingMore={subjects.isLoadingMore}
                            hasMore={subjects.hasMore}
                            onLoadMore={subjects.loadMore}
                            emptyMessage={t('common:messages.noData')}
                            searchPlaceholder={t('course:fields.searchSubject')}
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
                          <FormLabel>{t('course:fields.program')}</FormLabel>
                          <LoadMoreSelect
                            value={field.value}
                            onValueChange={field.onChange}
                            placeholder={t('course:fields.selectProgram')}
                            items={programs.selectItems}
                            isLoading={programs.isLoading}
                            isLoadingMore={programs.isLoadingMore}
                            hasMore={programs.hasMore}
                            onLoadMore={programs.loadMore}
                            emptyMessage={t('common:messages.noData')}
                            searchPlaceholder={t('course:fields.searchProgram')}
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
                          <FormLabel>{t('course:fields.code')}</FormLabel>
                          <FormControl>
                            <Input
                              placeholder={t('course:fields.codePlaceholder')}
                              {...field}
                              autoComplete='off'
                              onKeyDown={(e) => {
                                if (e.key === 'Enter') {
                                  e.preventDefault();
                                }
                              }}
                              disabled={isEditing}
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
                          <FormLabel>{t('course:fields.year')}</FormLabel>
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
                              placeholder={t('course:fields.yearPlaceholder')}
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
                          <FormLabel>{t('course:fields.semester')}</FormLabel>
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
                              placeholder={t(
                                'course:fields.semesterPlaceholder',
                              )}
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
                          <FormLabel>{t('course:fields.startDate')}</FormLabel>
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
                          <FormLabel>{t('course:fields.lecturer')}</FormLabel>
                          <FormControl>
                            <Input
                              placeholder={t(
                                'course:fields.lecturerPlaceholder',
                              )}
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
                          <FormLabel>{t('course:fields.maxStudent')}</FormLabel>
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
                              placeholder={t(
                                'course:fields.maxStudentPlaceholder',
                              )}
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
                          <FormLabel>{t('course:fields.schedule')}</FormLabel>
                          <FormControl>
                            <Input
                              placeholder={t(
                                'course:fields.schedulePlaceholder',
                              )}
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
                            {t('course:messages.scheduleFormat')}
                          </p>
                        </FormItem>
                      )}
                    />

                    <FormField
                      control={form.control}
                      name='room'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>{t('course:fields.room')}</FormLabel>
                          <FormControl>
                            <Input
                              placeholder={t('course:fields.roomPlaceholder')}
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
                      ? t('course:actions.update')
                      : t('course:actions.create')}
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
