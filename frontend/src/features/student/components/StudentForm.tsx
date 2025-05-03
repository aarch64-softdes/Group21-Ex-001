import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import * as z from 'zod';
import { useTranslation } from 'react-i18next';

import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from '@ui/accordion';
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
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@ui/select';
import { Separator } from '@ui/separator';

import AddressForm from '@student/components/AddressForm';
import PhoneField from '@student/components/PhoneField';
import { useGenders } from '@metadata/api/useMetadata';
import { useStudent } from '@student/api/useStudentApi';
import Student, { CreateStudentDTO } from '@student/types/student';
import { FormComponentProps } from '@/core/types/table';
import { Loader2 } from 'lucide-react';
import { useEffect, useState } from 'react';
import LoadingButton from '@ui/loadingButton';
import { cn } from '@/shared/lib/utils';
import { removeDialCodeFromPhoneNumber } from '@/shared/data/countryData';
import { useFacultiesDropdown } from '@/features/faculty/api/useFacultyApi';
import { useProgramsDropdown } from '@/features/program/api/useProgramApi';
import { useStatusesDropdown } from '@/features/status/api/useStatusApi';
import LoadMoreSelect from '@/components/common/LoadMoreSelect';
import { Textarea } from '@/components/ui/textarea';
import { t } from 'i18next';

export const StudentFormSchema = z.object({
  studentId: z.string().min(1, t('student:validation.required')),
  name: z
    .string()
    .min(1, t('student:validation.required'))
    .regex(/^[\p{L}\s]*$/u, t('student:validation.nameFormat')),
  dob: z.string().refine((date) => {
    const today = new Date();
    const birthDate = new Date(date);
    return birthDate < today;
  }, t('student:validation.pastDate')),
  gender: z.string().min(1, t('student:validation.required')),
  faculty: z.string().min(1, t('student:validation.required')),
  schoolYear: z.number().int().positive(),
  program: z.string(),
  email: z.string().email(t('student:validation.email')),
  address: z.string().optional(),
  phone: z.object({
    phoneNumber: z.string(),
    countryCode: z.string(),
  }),

  status: z
    .string()
    .min(1, t('student:validation.required'))
    .default('Studying'),

  permanentAddress: z.object({
    street: z.string().min(1, t('student:validation.required')),
    ward: z.string().min(1, t('student:validation.required')),
    district: z.string().min(1, t('student:validation.required')),
    province: z.string().min(1, t('student:validation.required')),
    country: z
      .string()
      .min(1, t('student:validation.required'))
      .default('Việt Nam'),
  }),

  temporaryAddress: z
    .object({
      street: z.string().optional(),
      ward: z.string().optional(),
      district: z.string().optional(),
      province: z.string().optional(),
      country: z.string().optional().default('Việt Nam'),
    })
    .optional()
    .or(
      z.object({
        street: z.string().min(1, t('student:validation.required')),
        ward: z.string().min(1, t('student:validation.required')),
        district: z.string().min(1, t('student:validation.required')),
        province: z.string().min(1, t('student:validation.required')),
        country: z
          .string()
          .min(1, t('student:validation.required'))
          .default('Việt Nam'),
      }),
    ),

  mailingAddress: z.object({
    street: z.string().min(1, t('student:validation.required')),
    ward: z.string().min(1, t('student:validation.required')),
    district: z.string().min(1, t('student:validation.required')),
    province: z.string().min(1, t('student:validation.required')),
    country: z
      .string()
      .min(1, t('student:validation.required'))
      .default('Việt Nam'),
  }),

  identity: z
    .object({
      type: z.enum(['Identity Card', 'Chip Card', 'Passport']),
    })
    .and(
      z.discriminatedUnion('type', [
        z.object({
          type: z.literal('Identity Card'),
          number: z
            .string()
            .min(9, t('student:validation.idLength', { length: 9 })),
          issuedDate: z.string().min(1, t('student:validation.required')),
          expiryDate: z.string().min(1, t('student:validation.required')),
          issuedBy: z.string().min(1, t('student:validation.required')),
          hasChip: z.boolean().optional(),
          country: z.string().optional(),
          notes: z.string().optional(),
        }),
        z.object({
          type: z.literal('Chip Card'),
          number: z
            .string()
            .min(12, t('student:validation.idLength', { length: 12 })),
          issuedDate: z.string().min(1, t('student:validation.required')),
          expiryDate: z.string().min(1, t('student:validation.required')),
          issuedBy: z.string().min(1, t('student:validation.required')),
          hasChip: z.boolean({
            required_error: t('student:validation.required'),
          }),
          country: z.string().optional(),
          notes: z.string().optional(),
        }),
        z.object({
          type: z.literal('Passport'),
          number: z
            .string()
            .min(9, t('student:validation.idLength', { length: 9 }))
            .max(9, t('student:validation.idLength', { length: 9 }))
            .regex(
              /^[A-Z]{2}[0-9]{7}$/,
              t('student:validation.passportFormat'),
            ),
          issuedDate: z.string().min(1, t('student:validation.required')),
          expiryDate: z.string().min(1, t('student:validation.required')),
          issuedBy: z.string().min(1, t('student:validation.required')),
          hasChip: z.boolean().optional(),
          country: z.string().min(1, t('student:validation.required')),
          notes: z.string().optional(),
        }),
      ]),
    ),
});

export type StudentFormValues = z.infer<typeof StudentFormSchema>;

const FormSection = ({
  title,
  children,
}: {
  title: string;
  children: React.ReactNode;
}) => (
  <Card className='mb-6 bg-zinc -50'>
    <CardHeader>
      <CardTitle className='text-lg font-medium'>{title}</CardTitle>
      <Separator />
    </CardHeader>
    <CardContent>{children}</CardContent>
  </Card>
);

const identityTypes = [
  { type: 'Identity Card', displayName: 'Identity Card (CMND)' },
  { type: 'Chip Card', displayName: 'Chip Card (CCCD)' },
  { type: 'Passport', displayName: 'Passport' },
];

const StudentForm: React.FC<FormComponentProps<Student>> = ({
  onSubmit,
  onCancel,
  id,
  isLoading = false,
  isEditing = false,
}) => {
  const { t } = useTranslation(['student', 'common']);

  const faculties = useFacultiesDropdown(isEditing ? 100 : 5);
  const programs = useProgramsDropdown(isEditing ? 100 : 5);
  const statuses = useStatusesDropdown(isEditing ? 100 : 5);

  const gendersQuery = useGenders();

  const { data: studentData, isLoading: isLoadingStudent } = useStudent(
    id ?? '',
  );

  // Add state to force select controls to rerender
  const [formKey, setFormKey] = useState(id || 'new');

  const addressTypes = [
    {
      type: 'permanentAddress',
      displayName: t('student:fields.address.permanent'),
    },
    {
      type: 'temporaryAddress',
      displayName: t('student:fields.address.temporary'),
    },
    {
      type: 'mailingAddress',
      displayName: t('student:fields.address.mailing'),
    },
  ];

  const form = useForm<StudentFormValues>({
    resolver: zodResolver(StudentFormSchema),
    defaultValues: {
      studentId: '',
      name: '',
      dob: '',
      gender: '',
      faculty: '',
      schoolYear: 1,
      program: '',
      email: '',
      phone: {
        phoneNumber: '',
        countryCode: 'VN',
      },
      status: 'Studying',
      permanentAddress: {
        street: '',
        ward: '',
        district: '',
        province: '',
        country: 'Việt Nam',
      },
      temporaryAddress: {
        street: '',
        ward: '',
        district: '',
        province: '',
        country: 'Việt Nam',
      },
      mailingAddress: {
        street: '',
        ward: '',
        district: '',
        province: '',
        country: 'Việt Nam',
      },
    },
  });

  const formatDateForInput = (date: Date | string): string => {
    if (!date) return '';

    const d = typeof date === 'string' ? new Date(date) : date;
    return d.toISOString().split('T')[0]; // Returns YYYY-MM-DD
  };

  // Update formKey when id changes to force remount
  useEffect(() => {
    setFormKey(id || 'new');
  }, [id]);

  // Reset form when student data is loaded
  useEffect(() => {
    if (studentData && id) {
      const formattedDob =
        studentData.dob instanceof Date
          ? formatDateForInput(studentData.dob)
          : formatDateForInput(studentData.dob as string);

      console.log('Student data loaded:', studentData);

      // Ensure all values are properly formatted
      const formValues = {
        studentId: studentData.studentId || id,
        name: studentData.name || '',
        dob: formattedDob || '',
        gender: studentData.gender || '',
        faculty: studentData.faculty || '',
        schoolYear: parseInt(studentData.schoolYear?.toString() || '1', 10),
        program: studentData.program || '',
        email: studentData.email || '',
        phone: {
          phoneNumber:
            removeDialCodeFromPhoneNumber(
              studentData.phone?.phoneNumber,
              studentData.phone?.countryCode,
            ) || '',
          countryCode: studentData.phone?.countryCode || 'VN',
        },
        status: studentData.status || 'Studying',
        permanentAddress: studentData.permanentAddress || {
          street: '',
          ward: '',
          district: '',
          province: '',
          country: 'Việt Nam',
        },
        temporaryAddress: studentData.temporaryAddress || {
          street: '',
          ward: '',
          district: '',
          province: '',
          country: 'Việt Nam',
        },
        mailingAddress: studentData.mailingAddress || {
          street: '',
          ward: '',
          district: '',
          province: '',
          country: 'Việt Nam',
        },
        identity: studentData.identity || {
          type: 'Identity Card',
          number: '',
          issuedDate: '',
          expiryDate: '',
          issuedBy: '',
        },
      };

      // Log values being set for debugging
      console.log('Setting form values:', formValues);

      // Use setTimeout to ensure the form resets after the component has fully rendered
      setTimeout(() => {
        form.reset(formValues);
      }, 0);
    }
  }, [studentData, id, form]);

  const [addressAccordionOpen, setAddressAccordionOpen] = useState<
    string | undefined
  >(undefined);

  useEffect(() => {
    const { errors } = form.formState;
    console.log('Form errors:', errors);

    if (
      errors.permanentAddress ||
      errors.temporaryAddress ||
      errors.mailingAddress
    ) {
      setAddressAccordionOpen('detailed-addresses');
    }
  }, [form.formState.errors]);

  const handleSubmit = (values: StudentFormValues) => {
    console.log('Submitting form with values:', values);

    const formattedValues = {
      ...values,
      dob: new Date(values.dob),
    };

    onSubmit(formattedValues as unknown as CreateStudentDTO);
  };

  const handleError = (errors: any) => {
    console.error('Form submission failed with errors:', errors);

    if (
      errors.permanentAddress ||
      errors.temporaryAddress ||
      errors.mailingAddress
    ) {
      setAddressAccordionOpen('detailed-addresses');
    }
  };

  // Determine if we should show loading state
  const isFormLoading = isLoading || (isEditing && isLoadingStudent);

  // Use form.watch() to debug values
  const watchedValues = {
    gender: form.watch('gender'),
    faculty: form.watch('faculty'),
    program: form.watch('program'),
    status: form.watch('status'),
    phone: form.watch('phone'),
  };

  // Log watched values for debugging
  useEffect(() => {
    console.log('Watched form values:', watchedValues);
  }, [
    watchedValues.gender,
    watchedValues.faculty,
    watchedValues.program,
    watchedValues.status,
    watchedValues.phone,
  ]);

  return (
    <div className='sticky inset-0'>
      {/* Header */}
      <div className='border-b px-4 py-2 flex items-center justify-between'>
        <h2 className='text-xl font-semibold'>
          {isEditing ? t('student:editStudent') : t('student:addNew')}
        </h2>
      </div>

      {/* Content */}
      <div className='flex-1/2  p-4 min-h-0'>
        <div className='max-w-5xl mx-auto pb-4'>
          <Form {...form}>
            {isFormLoading ? (
              <div className='flex items-center justify-center p-6'>
                <Loader2 className='h-8 w-8 animate-spin' />
              </div>
            ) : (
              <form
                key={formKey} // Force remount when student changes
                onSubmit={form.handleSubmit(handleSubmit, handleError)}
                className='max-w-5xl max-auto'
                autoComplete='off'
                noValidate
              >
                <FormSection title={t('student:sections.basicInfo')}>
                  <div className='grid grid-cols-2 lg:grid-cols-3 gap-6'>
                    <FormField
                      control={form.control}
                      name='studentId'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>{t('student:fields.studentId')}</FormLabel>
                          <FormControl>
                            <Input
                              placeholder='e.g. S12345'
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
                      name='name'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>{t('student:fields.name')}</FormLabel>
                          <FormControl>
                            <Input
                              placeholder='John Doe'
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
                      name='dob'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>{t('student:fields.dob')}</FormLabel>
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
                      name='gender'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>{t('student:fields.gender')}</FormLabel>
                          <Select
                            onValueChange={field.onChange}
                            value={field.value || undefined}
                          >
                            <FormControl>
                              <SelectTrigger>
                                <SelectValue
                                  placeholder={t('student:fields.selectGender')}
                                />
                              </SelectTrigger>
                            </FormControl>
                            <SelectContent>
                              {gendersQuery.isLoading ? (
                                <div className='flex items-center justify-center p-2'>
                                  <Loader2 className='h-4 w-4 animate-spin' />
                                </div>
                              ) : gendersQuery.data ? (
                                gendersQuery.data.map((gender) => (
                                  <SelectItem key={gender} value={gender}>
                                    {gender.replace(/_/g, ' ')}
                                  </SelectItem>
                                ))
                              ) : (
                                <SelectItem value='' disabled>
                                  {t('common:messages.noData')}
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
                      name='email'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>{t('student:fields.email')}</FormLabel>
                          <FormControl>
                            <Input
                              placeholder='example@email.com'
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

                    <div className='lg:col-span-1'>
                      <PhoneField form={form} />
                    </div>
                  </div>

                  <div className='mt-6'>
                    <Accordion
                      type='single'
                      collapsible
                      className='w-full'
                      value={addressAccordionOpen}
                      onValueChange={setAddressAccordionOpen}
                    >
                      <AccordionItem value='detailed-addresses'>
                        <AccordionTrigger className='font-medium text-base'>
                          <div
                            className={cn(
                              (form.formState.errors.permanentAddress ||
                                form.formState.errors.temporaryAddress ||
                                form.formState.errors.mailingAddress) &&
                                'text-destructive',
                            )}
                          >
                            {t('student:sections.contactInfo')}
                            {(form.formState.errors.permanentAddress ||
                              form.formState.errors.temporaryAddress ||
                              form.formState.errors.mailingAddress) && (
                              <span className='text-destructive ml-2'>●</span>
                            )}
                          </div>
                        </AccordionTrigger>
                        <AccordionContent>
                          <div className='space-y-6 pt-2'>
                            {addressTypes.map((addressType) => (
                              <AddressForm
                                key={addressType.type}
                                form={form}
                                type={
                                  addressType.type as
                                    | 'permanentAddress'
                                    | 'temporaryAddress'
                                    | 'mailingAddress'
                                }
                                title={addressType.displayName}
                              />
                            ))}
                          </div>
                        </AccordionContent>
                      </AccordionItem>
                    </Accordion>
                  </div>
                </FormSection>

                <FormSection title={t('student:sections.identityInfo')}>
                  <div className='grid grid-cols-2 lg:grid-cols-3 gap-6'>
                    <FormField
                      control={form.control}
                      name='identity.type'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>
                            {t('student:fields.identity.type')}
                          </FormLabel>
                          <Select
                            onValueChange={field.onChange}
                            value={field.value || undefined}
                          >
                            <FormControl>
                              <SelectTrigger>
                                <SelectValue
                                  placeholder={t(
                                    'student:fields.identity.selectType',
                                  )}
                                />
                              </SelectTrigger>
                            </FormControl>
                            <SelectContent>
                              {identityTypes.map((type) => (
                                <SelectItem key={type.type} value={type.type}>
                                  {t(
                                    `student:fields.identity.types.${type.type
                                      .toLowerCase()
                                      .replace(' ', '')}`,
                                    type.displayName,
                                  )}
                                </SelectItem>
                              ))}
                            </SelectContent>
                          </Select>
                          <FormMessage />
                        </FormItem>
                      )}
                    />

                    <FormField
                      control={form.control}
                      name='identity.number'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>
                            {t('student:fields.identity.number')}
                          </FormLabel>
                          <FormControl>
                            <Input
                              placeholder='e.g. 012345678912'
                              {...field}
                              autoComplete='off'
                            />
                          </FormControl>
                          <FormMessage />
                        </FormItem>
                      )}
                    />

                    <FormField
                      control={form.control}
                      name='identity.issuedDate'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>
                            {t('student:fields.identity.issueDate')}
                          </FormLabel>
                          <FormControl>
                            <Input type='date' {...field} autoComplete='off' />
                          </FormControl>
                          <FormMessage />
                        </FormItem>
                      )}
                    />

                    <FormField
                      control={form.control}
                      name='identity.expiryDate'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>
                            {t('student:fields.identity.expiryDate')}
                          </FormLabel>
                          <FormControl>
                            <Input type='date' {...field} autoComplete='off' />
                          </FormControl>
                          <FormMessage />
                        </FormItem>
                      )}
                    />

                    <FormField
                      control={form.control}
                      name='identity.issuedBy'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>
                            {t('student:fields.identity.issuedBy')}
                          </FormLabel>
                          <FormControl>
                            <Input
                              placeholder='e.g. Ministry of Public Security'
                              {...field}
                              autoComplete='off'
                            />
                          </FormControl>
                          <FormMessage />
                        </FormItem>
                      )}
                    />

                    {/* Conditional fields based on document type */}
                    {form.watch('identity.type') === 'Chip Card' && (
                      <FormField
                        control={form.control}
                        name='identity.hasChip'
                        render={({ field }) => (
                          <FormItem className='flex flex-row items-start space-x-3 space-y-0 rounded-md border p-4'>
                            <FormControl>
                              <input
                                type='checkbox'
                                checked={field.value}
                                onChange={field.onChange}
                                className='h-4 w-4 mt-1'
                              />
                            </FormControl>
                            <div className='space-y-1 leading-none'>
                              <FormLabel>
                                {t('student:fields.identity.hasChip')}
                              </FormLabel>
                              <p className='text-sm text-muted-foreground'>
                                {t(
                                  'student:fields.identity.hasChipDescription',
                                )}
                              </p>
                            </div>
                            <FormMessage />
                          </FormItem>
                        )}
                      />
                    )}

                    {form.watch('identity.type') === 'Passport' && (
                      <>
                        <FormField
                          control={form.control}
                          name='identity.country'
                          render={({ field }) => (
                            <FormItem>
                              <FormLabel>
                                {t('student:fields.identity.country')}
                              </FormLabel>
                              <FormControl>
                                <Input
                                  placeholder='e.g. Việt Nam'
                                  {...field}
                                  autoComplete='off'
                                />
                              </FormControl>
                              <FormMessage />
                            </FormItem>
                          )}
                        />

                        <FormField
                          control={form.control}
                          name='identity.notes'
                          render={({ field }) => (
                            <FormItem className='col-span-2'>
                              <FormLabel>
                                {t('student:fields.identity.notes', 'Notes')}
                              </FormLabel>
                              <FormControl>
                                <Textarea
                                  placeholder={t(
                                    'student:fields.identity.notesPlaceholder',
                                    'Any additional information',
                                  )}
                                  {...field}
                                  autoComplete='off'
                                  rows={3}
                                  className='resize-none'
                                />
                              </FormControl>
                              <FormMessage />
                            </FormItem>
                          )}
                        />
                      </>
                    )}
                  </div>
                </FormSection>
                <FormSection title={t('student:sections.academicInfo')}>
                  <div className='grid grid-cols-2 gap-4'>
                    <FormField
                      control={form.control}
                      name='faculty'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>{t('student:fields.faculty')}</FormLabel>
                          <LoadMoreSelect
                            value={field.value || ''}
                            onValueChange={field.onChange}
                            placeholder={t('student:fields.selectFaculty')}
                            items={faculties.selectItems}
                            isLoading={faculties.isLoading}
                            isLoadingMore={faculties.isLoadingMore}
                            hasMore={faculties.hasMore}
                            onLoadMore={faculties.loadMore}
                            disabled={faculties.isLoading}
                            emptyMessage={t('common:messages.noData')}
                            searchPlaceholder={t(
                              'student:fields.searchFaculty',
                            )}
                            onSearch={faculties.setFacultySearch}
                          />
                          <FormMessage />
                        </FormItem>
                      )}
                    />

                    <FormField
                      control={form.control}
                      name='schoolYear'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>
                            {t('student:fields.schoolYear')}
                          </FormLabel>
                          <FormControl>
                            <Input
                              type='number'
                              min='1'
                              max='6'
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
                      name='program'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>{t('student:fields.program')}</FormLabel>
                          <LoadMoreSelect
                            value={field.value || ''}
                            onValueChange={field.onChange}
                            placeholder={t('student:fields.selectProgram')}
                            items={programs.selectItems}
                            isLoading={programs.isLoading}
                            isLoadingMore={programs.isLoadingMore}
                            hasMore={programs.hasMore}
                            onLoadMore={programs.loadMore}
                            disabled={programs.isLoading}
                            emptyMessage={t('common:messages.noData')}
                            searchPlaceholder={t(
                              'student:fields.searchProgram',
                            )}
                            onSearch={programs.setProgramSearch}
                          />
                          <FormMessage />
                        </FormItem>
                      )}
                    />

                    <FormField
                      control={form.control}
                      name='status'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>{t('student:fields.status')}</FormLabel>
                          <LoadMoreSelect
                            value={field.value || ''}
                            onValueChange={field.onChange}
                            placeholder={t('student:fields.selectStatus')}
                            items={statuses.selectItems}
                            isLoading={statuses.isLoading}
                            isLoadingMore={statuses.isLoadingMore}
                            hasMore={statuses.hasMore}
                            onLoadMore={statuses.loadMore}
                            disabled={statuses.isLoading}
                            emptyMessage={t('common:messages.noData')}
                            searchPlaceholder={t('student:fields.searchStatus')}
                            onSearch={statuses.setStatusSearch}
                          />
                          <FormMessage />
                        </FormItem>
                      )}
                    />
                  </div>
                </FormSection>

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

export default StudentForm;
