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
import { useEffect, useState } from 'react';
import LoadingButton from '@ui/loadingButton';
import { Badge } from '@/components/ui/badge';
import LoadMoreSelect from '@/components/common/LoadMoreSelect';
import { useFacultiesDropdown } from '@faculty/api/useFacultyApi';

// Define schema
export const SubjectFormSchema = z.object({
  name: z
    .string()
    .min(1, 'Name is required')
    .max(255, 'Name must be less than 255 characters'),
  code: z
    .string()
    .min(1, 'Code is required')
    .max(20, 'Code must be less than 20 characters'),
  credits: z
    .number()
    .min(2, 'Credits must be at least 2')
    .max(10, 'Credits must be at most 10')
    .or(z.string().regex(/^\d+$/).transform(Number)),
  description: z
    .string()
    .max(1000, 'Description must be less than 1000 characters')
    .optional(),
  facultyId: z.string().optional(),
  prerequisitesId: z.array(z.string()).optional().default([]),
});

export type SubjectFormValues = z.infer<typeof SubjectFormSchema>;

const SubjectForm: React.FC<FormComponentPropsWithoutType> = ({
  onSubmit,
  onCancel,
  id,
  isLoading = false,
  isEditing = false,
}) => {
  const { data: subjectData, isLoading: isLoadingSubject } = useSubject(
    id || '',
  );
  const faculties = useFacultiesDropdown(5, (faculty) => ({
    id: faculty.id,
    label: faculty.name,
    value: faculty.id,
  }));

  // Use the subjects dropdown hook
  const subjects = useSubjectsDropdown(isEditing ? 100 : 5, id);

  // Store selected prerequisites
  const [selectedPrerequisites, setSelectedPrerequisites] = useState<
    { id: string; label: string; value: string }[]
  >([]);

  const form = useForm<SubjectFormValues>({
    resolver: zodResolver(SubjectFormSchema),
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

      // Initialize selected prerequisites from subject data
      if (subjectData.prerequisites?.length) {
        const prereqs = subjectData.prerequisites.map((subject) => ({
          id: subject.id || '',
          label: `${subject.code} - ${subject.name}`,
          value: subject.id || '',
        }));
        setSelectedPrerequisites(prereqs);
      }
    }
  }, [subjectData, id, form]);

  const handleSubmit = (values: SubjectFormValues) => {
    onSubmit({
      ...values,
      prerequisitesId: values.prerequisitesId || [],
    });
  };

  // Handle adding a prerequisite
  const handleAddPrerequisite = (selectedId: string) => {
    if (!selectedId) return;

    // Find the selected item from the dropdown options
    const selectedItem = subjects.selectItems.find(
      (item) => item.id === selectedId,
    );
    if (
      selectedItem &&
      !selectedPrerequisites.some((item) => item.id === selectedId)
    ) {
      setSelectedPrerequisites((prev) => [...prev, selectedItem]);
    }
  };

  // Handle removing a prerequisite
  const handleRemovePrerequisite = (prerequisiteId: string) => {
    setSelectedPrerequisites((prev) =>
      prev.filter((item) => item.id !== prerequisiteId),
    );
  };

  // Determine if we should show loading state
  const isFormLoading = isLoading || (isEditing && isLoadingSubject);

  return (
    <div className='sticky inset-0'>
      {/* Header */}
      <div className='border-b px-4 py-2 flex items-center justify-between'>
        <h2 className='text-xl font-semibold'>
          {isEditing ? 'Edit Subject' : 'Add New Subject'}
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
                      Subject Information
                    </CardTitle>
                    <Separator />
                  </CardHeader>
                  <CardContent className='grid grid-cols-1 md:grid-cols-2 gap-6'>
                    <FormField
                      control={form.control}
                      name='name'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>Name</FormLabel>
                          <FormControl>
                            <Input
                              placeholder='Enter subject name'
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
                          <FormLabel>Credits</FormLabel>
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
                          <FormLabel>Faculty</FormLabel>
                          <LoadMoreSelect
                            value={field.value || ''}
                            onValueChange={field.onChange}
                            placeholder='Select faculty'
                            items={faculties.selectItems}
                            isLoading={faculties.isLoading}
                            isLoadingMore={faculties.isLoadingMore}
                            hasMore={faculties.hasMore}
                            onLoadMore={faculties.loadMore}
                            disabled={faculties.isLoading}
                            emptyMessage='No faculties found.'
                            searchPlaceholder='Search faculties...'
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
                        render={() => (
                          <FormItem className='col-span-2'>
                            <FormLabel>Prerequisites</FormLabel>
                            <div>
                              <div className='flex flex-wrap gap-2 mb-2 min-h-10 border rounded-md p-2'>
                                {selectedPrerequisites.length > 0 ? (
                                  selectedPrerequisites.map((prerequisite) => (
                                    <Badge
                                      key={prerequisite.id}
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
                                            prerequisite.id,
                                          );
                                        }}
                                      >
                                        <X className='h-3 w-3' />
                                      </button>
                                    </Badge>
                                  ))
                                ) : (
                                  <span className='text-sm text-muted-foreground'>
                                    No prerequisites selected
                                  </span>
                                )}
                              </div>
                              <LoadMoreSelect
                                value=''
                                onValueChange={handleAddPrerequisite}
                                placeholder='Add prerequisite'
                                items={subjects.selectItems}
                                isLoading={subjects.isLoading}
                                isLoadingMore={subjects.isLoadingMore}
                                hasMore={subjects.hasMore}
                                onLoadMore={subjects.loadMore}
                                disabled={subjects.isLoading}
                                emptyMessage='No prerequisites found.'
                                searchPlaceholder='Search prerequisites...'
                                onSearch={subjects.setSubjectSearch}
                              />
                              <FormMessage />
                            </div>
                          </FormItem>
                        )}
                      />
                    )}

                    <FormField
                      control={form.control}
                      name='description'
                      render={({ field }) => (
                        <FormItem className='col-span-2'>
                          <FormLabel>Description</FormLabel>
                          <FormControl>
                            <Textarea
                              placeholder='Enter subject description'
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
                    Cancel
                  </Button>
                  <LoadingButton type='submit' isLoading={isLoading}>
                    {isEditing ? 'Save Changes' : 'Add Subject'}
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
