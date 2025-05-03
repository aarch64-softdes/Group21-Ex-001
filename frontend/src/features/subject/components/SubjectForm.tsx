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
import { Textarea } from '@ui/textarea';
import { useSubject, useSubjectsDropdown } from '@subject/api/useSubjectApi';
import { FormComponentPropsWithoutType } from '@/core/types/table';
import { Loader2, X } from 'lucide-react';
import { useEffect } from 'react';
import LoadingButton from '@ui/loadingButton';
import { Badge } from '@/components/ui/badge';
import LoadMoreSelect from '@/components/common/LoadMoreSelect';
import { useFacultiesDropdown } from '@faculty/api/useFacultyApi';
import { useTranslation } from 'react-i18next';

// Define schema
export const SubjectFormSchema = (t: any) =>
  z.object({
    name: z
      .string()
      .min(1, t('subject:validation.nameRequired'))
      .max(255, t('subject:validation.nameTooLong')),
    code: z
      .string()
      .min(1, t('subject:validation.codeRequired'))
      .max(20, t('subject:validation.codeTooLong')),
    credits: z
      .number({ invalid_type_error: t('subject:validation.creditsInvalid') })
      .min(2, t('subject:validation.creditsMin'))
      .max(10, t('subject:validation.creditsMax'))
      .or(z.string().regex(/^\d+$/).transform(Number)),
    description: z
      .string()
      .max(1000, t('subject:validation.descriptionTooLong'))
      .optional(),
    facultyId: z.string().min(1, t('subject:validation.facultyRequired')),
    prerequisitesId: z.array(z.string()).optional().default([]),
  });

export type SubjectFormValues = z.infer<ReturnType<typeof SubjectFormSchema>>;

const SubjectForm: React.FC<FormComponentPropsWithoutType> = ({
  onSubmit,
  onCancel,
  id,
  isLoading = false,
  isEditing = false,
}) => {
  const { t } = useTranslation(['subject', 'common']);

  const { data: subjectData, isLoading: isLoadingSubject } = useSubject(
    id || '',
  );
  const faculties = useFacultiesDropdown(5, (faculty) => ({
    id: faculty.id,
    label: faculty.name,
    value: faculty.id,
  }));

  const subjects = useSubjectsDropdown(
    5,
    (subject) => ({
      id: subject.id,
      label: `${subject.code} - ${subject.name}`,
      value: subject.id,
      metadata: {
        isActive: subject.isActive,
      },
    }),
    id,
  );

  const form = useForm<SubjectFormValues>({
    resolver: zodResolver(SubjectFormSchema(t)),
    defaultValues: {
      name: '',
      code: '',
      credits: 3,
      description: '',
      facultyId: '',
      prerequisitesId: [],
    },
  });

  // Reset form when subject data is loaded
  useEffect(() => {
    if (subjectData && id) {
      form.reset({
        name: subjectData.name || '',
        code: subjectData.code || '',
        credits: subjectData.credits || 3,
        description: subjectData.description || '',
        facultyId: subjectData.faculty.id || '',
        prerequisitesId:
          subjectData.prerequisites?.map((subject) => subject.id) || [],
      });

      if (subjectData.faculty.id) {
        faculties.setItem({
          id: subjectData.faculty.id,
          label: subjectData.faculty.name || '',
          value: subjectData.faculty.id,
        });
      }
    }
  }, [subjectData, id, form]);

  const handleSubmit = (values: SubjectFormValues) => {
    if (isEditing) {
      let { code, ...updateData } = values;
      onSubmit(updateData);
    } else {
      onSubmit(values);
    }
  };

  // Determine if we should show loading state
  const isFormLoading = isLoading || (isEditing && isLoadingSubject);

  return (
    <div className='sticky inset-0'>
      {/* Header */}
      <div className='border-b px-4 py-2 flex items-center justify-between'>
        <h2 className='text-xl font-semibold'>
          {isEditing ? t('subject:editSubject') : t('subject:addNew')}
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
                      {t('subject:sections.subjectInfo')}
                    </CardTitle>
                    <Separator />
                  </CardHeader>
                  <CardContent className='grid grid-cols-1 md:grid-cols-2 gap-6'>
                    <FormField
                      control={form.control}
                      name='name'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>{t('subject:fields.name')}</FormLabel>
                          <FormControl>
                            <Input
                              placeholder={t('subject:fields.namePlaceholder')}
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
                      name='code'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>{t('subject:fields.code')}</FormLabel>
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
                              disabled={isEditing}
                            />
                          </FormControl>
                          <FormMessage />
                        </FormItem>
                      )}
                    />

                    <FormField
                      control={form.control}
                      name='credits'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>{t('subject:fields.credits')}</FormLabel>
                          <FormControl>
                            <Input
                              type='number'
                              min={1}
                              max={10}
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
                      name='facultyId'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>{t('subject:fields.faculty')}</FormLabel>
                          <LoadMoreSelect
                            value={field.value || ''}
                            onValueChange={field.onChange}
                            placeholder={t('subject:fields.selectFaculty')}
                            items={faculties.selectItems}
                            isLoading={faculties.isLoading}
                            isLoadingMore={faculties.isLoadingMore}
                            hasMore={faculties.hasMore}
                            onLoadMore={faculties.loadMore}
                            disabled={faculties.isLoading}
                            emptyMessage={t('common:messages.noData')}
                            searchPlaceholder={t(
                              'subject:fields.searchFaculty',
                            )}
                            onSearch={faculties.setFacultySearch}
                          />
                          <FormMessage />
                        </FormItem>
                      )}
                    />

                    {!isEditing && (
                      <FormField
                        control={form.control}
                        name='prerequisitesId'
                        render={({ field }) => {
                          const handleAddPrerequisite = (
                            selectedId: string,
                          ) => {
                            if (!selectedId) return;

                            const selectedItem = subjects.selectItems.find(
                              (item) => item.id === selectedId,
                            );
                            if (
                              selectedItem &&
                              !field.value.includes(selectedId)
                            ) {
                              field.onChange([...field.value, selectedId]);
                            }
                          };

                          const handleRemovePrerequisite = (
                            prerequisiteId: string,
                          ) => {
                            field.onChange(
                              field.value.filter(
                                (id: string) => id !== prerequisiteId,
                              ),
                            );
                          };

                          return (
                            <FormItem className='col-span-2'>
                              <FormLabel>
                                {t('subject:fields.prerequisites')}
                              </FormLabel>
                              <div>
                                <div className='flex flex-wrap gap-2 mb-2 min-h-10 border rounded-md p-2'>
                                  {field.value && field.value.length > 0 ? (
                                    field.value.map((prerequisiteId) => {
                                      const prerequisite =
                                        subjects.selectItems.find(
                                          (item) => item.id === prerequisiteId,
                                        );
                                      if (!prerequisite) return null;

                                      return (
                                        <Badge
                                          key={prerequisiteId}
                                          variant='secondary'
                                          className='flex items-center gap-1'
                                        >
                                          <div>{prerequisite.label}</div>
                                          <button
                                            type='button'
                                            className='ml-1 p-1 rounded-full hover:bg-gray-200 focus:outline-none'
                                            onClick={(e) => {
                                              e.stopPropagation();
                                              e.preventDefault();
                                              handleRemovePrerequisite(
                                                prerequisiteId,
                                              );
                                            }}
                                          >
                                            <X className='h-3 w-3' />
                                          </button>
                                        </Badge>
                                      );
                                    })
                                  ) : (
                                    <span className='text-sm text-muted-foreground'>
                                      {t('subject:messages.noPrerequisites')}
                                    </span>
                                  )}
                                </div>
                                <LoadMoreSelect
                                  value=''
                                  onValueChange={handleAddPrerequisite}
                                  placeholder={t(
                                    'subject:fields.selectPrerequisites',
                                  )}
                                  items={subjects.selectItems.filter(
                                    (item) => !field.value.includes(item.id),
                                  )}
                                  isLoading={subjects.isLoading}
                                  isLoadingMore={subjects.isLoadingMore}
                                  hasMore={subjects.hasMore}
                                  onLoadMore={subjects.loadMore}
                                  disabled={subjects.isLoading}
                                  emptyMessage={t('common:messages.noData')}
                                  searchPlaceholder={t(
                                    'subject:fields.searchPrerequisites',
                                  )}
                                  onSearch={subjects.setSubjectSearch}
                                  disabledItems={(metadata) => {
                                    return !metadata.isActive;
                                  }}
                                />
                                <FormMessage />
                              </div>
                            </FormItem>
                          );
                        }}
                      />
                    )}

                    <FormField
                      control={form.control}
                      name='description'
                      render={({ field }) => (
                        <FormItem className='col-span-2'>
                          <FormLabel>
                            {t('subject:fields.description')}
                          </FormLabel>
                          <FormControl>
                            <Textarea
                              placeholder={t(
                                'subject:fields.descriptionPlaceholder',
                              )}
                              className='min-h-[100px]'
                              {...field}
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
                      ? t('subject:actions.update')
                      : t('subject:actions.create')}
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

export default SubjectForm;
