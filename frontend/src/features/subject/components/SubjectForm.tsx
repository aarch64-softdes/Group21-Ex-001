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
import {
  useSubject,
  useSubjectsForPrerequisites,
} from '@subject/api/useSubjectApi';
import Subject, {
  CreateSubjectDTO,
  UpdateSubjectDTO,
} from '@subject/types/subject';
import {
  FormComponentProps,
  FormComponentPropsWithoutType,
} from '@/core/types/table';
import { Loader2, X } from 'lucide-react';
import { useEffect, useState } from 'react';
import LoadingButton from '@ui/loadingButton';
import { Badge } from '@/components/ui/badge';
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover';
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
} from '@/components/ui/command';
import { Check, ChevronsUpDown } from 'lucide-react';
import { cn } from '@/shared/lib/utils';
import { useFacultiesDropdown2 } from '@faculty/api/useFacultyApi';
import LoadMoreSelect from '@/components/common/LoadMoreSelect';

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
    .min(1, 'Credits must be at least 1')
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
  const faculties = useFacultiesDropdown2(isEditing ? 100 : 5);
  const subjectsQuery = useSubjectsForPrerequisites(id);

  // Store all subjects for displaying names in the UI
  const [allSubjects, setAllSubjects] = useState<
    { id: string; name: string; code: string }[]
  >([]);

  // Update allSubjects when the query data changes
  useEffect(() => {
    if (subjectsQuery.data) {
      setAllSubjects(
        subjectsQuery.data.map((subject) => ({
          id: subject.id,
          name: subject.name,
          code: subject.code,
        })),
      );
    }
  }, [subjectsQuery.data]);

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
        prerequisitesId:
          subjectData.prerequisites?.map((subject) => subject.id) || [],
      });
    }
  }, [subjectData, id, form]);

  useEffect(() => {
    if (faculties.items.length > 0 && subjectData?.faculty) {
      const matchingFaculty = faculties.items.find(
        (f) => f.name === subjectData.faculty,
      );
      if (matchingFaculty) {
        form.setValue('facultyId', matchingFaculty.id.toString());
      }
    }
  }, [faculties.items, subjectData, form]);

  const handleSubmit = (values: SubjectFormValues) => {
    onSubmit({
      ...values,
      prerequisitesId: values.prerequisitesId || [],
    });
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

                    <FormField
                      control={form.control}
                      name='prerequisitesId'
                      render={({ field }) => (
                        <FormItem className='col-span-2'>
                          <FormLabel>Prerequisites</FormLabel>
                          <div>
                            <div className='flex flex-wrap gap-2 mb-2 min-h-10 border rounded-md p-2'>
                              {field.value && field.value.length > 0 ? (
                                field.value.map((prerequisiteId) => {
                                  const prerequisite = allSubjects?.find(
                                    (s) => s.id === prerequisiteId,
                                  );
                                  return (
                                    <Badge
                                      key={prerequisiteId}
                                      variant='secondary'
                                      className='flex items-center gap-1'
                                    >
                                      <div>
                                        {prerequisite
                                          ? `${prerequisite.code} - ${prerequisite.name}`
                                          : prerequisiteId}
                                      </div>
                                      <button
                                        type='button'
                                        className='ml-1 p-1 rounded-full hover:bg-gray-200 focus:outline-none'
                                        onClick={(e) => {
                                          e.stopPropagation();
                                          e.preventDefault();
                                          const updatedPrerequisites =
                                            field.value?.filter(
                                              (id) => id !== prerequisiteId,
                                            ) || [];
                                          field.onChange(updatedPrerequisites);
                                        }}
                                      >
                                        <X className='h-3 w-3' />
                                      </button>
                                    </Badge>
                                  );
                                })
                              ) : (
                                <span className='text-sm text-muted-foreground'>
                                  No prerequisites selected
                                </span>
                              )}
                            </div>
                            <Popover>
                              <PopoverTrigger asChild>
                                <Button
                                  variant='outline'
                                  role='combobox'
                                  className='w-full justify-between'
                                >
                                  Add prerequisite
                                  <ChevronsUpDown className='ml-2 h-4 w-4 shrink-0 opacity-50' />
                                </Button>
                              </PopoverTrigger>
                              <PopoverContent className='w-full p-0'>
                                <Command>
                                  <CommandInput placeholder='Search prerequisites...' />
                                  <CommandEmpty className='flex items-center justify-center pt-2 text-sm text-muted-foreground'>
                                    No prerequisite found.
                                  </CommandEmpty>
                                  <CommandGroup>
                                    {subjectsQuery.isLoading ? (
                                      <div className='flex items-center justify-center p-2'>
                                        <Loader2 className='h-4 w-4 animate-spin' />
                                      </div>
                                    ) : subjectsQuery.data ? (
                                      subjectsQuery.data
                                        .filter(
                                          (subject) =>
                                            !field.value?.includes(subject.id),
                                        )
                                        .map((subject) => (
                                          <CommandItem
                                            key={subject.id}
                                            value={subject.id}
                                            onSelect={(value) => {
                                              if (
                                                value &&
                                                !field.value?.includes(value)
                                              ) {
                                                const updatedPrerequisites = [
                                                  ...(field.value || []),
                                                  value,
                                                ];
                                                field.onChange(
                                                  updatedPrerequisites,
                                                );
                                              }
                                            }}
                                          >
                                            <Check
                                              className={cn(
                                                'mr-2 h-4 w-4',
                                                field.value?.includes(
                                                  subject.id,
                                                )
                                                  ? 'opacity-100'
                                                  : 'opacity-0',
                                              )}
                                            />
                                            {subject.code} - {subject.name}
                                          </CommandItem>
                                        ))
                                    ) : (
                                      <CommandItem value='' disabled>
                                        No subjects available
                                      </CommandItem>
                                    )}
                                  </CommandGroup>
                                </Command>
                              </PopoverContent>
                            </Popover>
                            <FormMessage />
                          </div>
                        </FormItem>
                      )}
                    />

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
