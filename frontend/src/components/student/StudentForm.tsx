import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import * as z from 'zod';

import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from '@/components/ui/accordion';
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
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { Separator } from '@/components/ui/separator';

import AddressForm from '@/components/student/AddressForm';
import PhoneField from '@/components/student/PhoneField';
import {
  useEntityFaculties,
  useEntityPrograms,
  useEntityStatuses,
  useGenders,
} from '@/hooks/api/useMetadata';
import { useStudent } from '@/hooks/api/useStudentApi';
import Student, { CreateStudentDTO } from '@/types/student';
import { FormComponentProps } from '@/types/table';
import { Loader2 } from 'lucide-react';
import { useEffect, useState } from 'react';
import LoadingButton from '../ui/loadingButton';
import { cn } from '@/lib/utils';
import {
  findCountryByCode,
  removeDialCodeFromPhoneNumber,
} from '@/data/countryData';

export const StudentFormSchema = z.object({
  studentId: z.string().min(1, 'Student ID is required'),
  name: z
    .string()
    .min(1, 'Name is required')
    .regex(/^[\p{L}\s]*$/u, 'Name must contain only letters and spaces'),
  dob: z.string().refine((date) => {
    const today = new Date();
    const birthDate = new Date(date);
    return birthDate < today;
  }, 'Date of birth must be in the past'),
  gender: z.string().min(1, 'Gender is required'),
  faculty: z.string().min(1, 'Faculty is required'),
  course: z.number().int().positive(),
  program: z.string(),
  email: z.string().email('Invalid email address'),
  address: z.string().optional(),
  phone: z.object({
    phoneNumber: z.string(),
    countryCode: z.string(),
  }),

  status: z.string().min(1, 'Status is required').default('Studying'),

  permanentAddress: z.object({
    street: z.string().min(1, 'Street address is required'),
    ward: z.string().min(1, 'Ward is required'),
    district: z.string().min(1, 'District is required'),
    province: z.string().min(1, 'Province is required'),
    country: z.string().min(1, 'Country is required').default('Việt Nam'),
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
        street: z
          .string()
          .min(1, 'If providing temporary address, street is required'),
        ward: z.string().min(1, 'Ward is required'),
        district: z.string().min(1, 'District is required'),
        province: z.string().min(1, 'Province is required'),
        country: z.string().min(1, 'Country is required').default('Việt Nam'),
      }),
    ),

  mailingAddress: z.object({
    street: z.string().min(1, 'Street address is required'),
    ward: z.string().min(1, 'Ward is required'),
    district: z.string().min(1, 'District is required'),
    province: z.string().min(1, 'Province is required'),
    country: z.string().min(1, 'Country is required').default('Việt Nam'),
  }),

  identity: z
    .object({
      type: z.enum(['Identity Card', 'Chip Card', 'Passport']),
    })
    .and(
      z.discriminatedUnion('type', [
        z.object({
          type: z.literal('Identity Card'),
          number: z.string().min(9, 'ID number must include 9 digits'),
          issuedDate: z.string().min(1, 'Issue date is required'),
          expiryDate: z.string().min(1, 'Expiration date is required'),
          issuedBy: z.string().min(1, 'Issuing authority is required'),
          hasChip: z.boolean().optional(),
          country: z.string().optional(),
          notes: z.string().optional(),
        }),
        z.object({
          type: z.literal('Chip Card'),
          number: z.string().min(12, 'ID number must include 12 digits'),
          issuedDate: z.string().min(1, 'Issue date is required'),
          expiryDate: z.string().min(1, 'Expiration date is required'),
          issuedBy: z.string().min(1, 'Issuing authority is required'),
          hasChip: z.boolean({
            required_error: 'Please specify if this ID card has a chip',
          }),
          country: z.string().optional(),
          notes: z.string().optional(),
        }),
        z.object({
          type: z.literal('Passport'),
          number: z
            .string()
            .min(9, 'Passport number must be 9 characters')
            .max(9, 'Passport number must be 9 characters')
            .regex(
              /^[A-Z]{2}[0-9]{7}$/,
              'Passport number must be 2 uppercase letters followed by 7 digits',
            ),
          issuedDate: z.string().min(1, 'Issue date is required'),
          expiryDate: z.string().min(1, 'Expiration date is required'),
          issuedBy: z.string().min(1, 'Issuing authority is required'),
          hasChip: z.boolean().optional(),
          country: z.string().min(1, 'Country is required for passport'),
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

const addressTypes = [
  { type: 'permanentAddress', displayName: 'Permanent Address' },
  { type: 'temporaryAddress', displayName: 'Temporary Address' },
  { type: 'mailingAddress', displayName: 'Mailing Address' },
];

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
  const facultiesQuery = useEntityFaculties();
  const programsQuery = useEntityPrograms();
  const statusesQuery = useEntityStatuses();
  const gendersQuery = useGenders();
  const { data: studentData, isLoading: isLoadingStudent } = useStudent(
    id ?? '',
  );

  // Add state to force select controls to rerender
  const [formKey, setFormKey] = useState(id || 'new');

  const form = useForm<StudentFormValues>({
    resolver: zodResolver(StudentFormSchema),
    defaultValues: {
      studentId: '',
      name: '',
      dob: '',
      gender: '',
      faculty: '',
      course: 1,
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
        course: parseInt(studentData.course?.toString() || '1', 10),
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
          {isEditing ? 'Edit Student' : 'Add New Student'}
        </h2>
      </div>

      {/* Content */}
      <div className='flex-1/2 overflow-y-auto p-4 min-h-0'>
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
                <FormSection title='Basic Information'>
                  <div className='grid grid-cols-2 lg:grid-cols-3 gap-6'>
                    <FormField
                      control={form.control}
                      name='studentId'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>Student ID</FormLabel>
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
                          <FormLabel>Name</FormLabel>
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
                          <FormLabel>Date of Birth</FormLabel>
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
                          <FormLabel>Gender</FormLabel>
                          <Select
                            onValueChange={field.onChange}
                            value={field.value || undefined}
                          >
                            <FormControl>
                              <SelectTrigger>
                                <SelectValue placeholder='Select gender' />
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
                                  Failed to load genders
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
                          <FormLabel>Email</FormLabel>
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
                            Contact Addresses
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

                <FormSection title='Identity Information'>
                  <div className='grid grid-cols-2 lg:grid-cols-3 gap-6'>
                    <FormField
                      control={form.control}
                      name='identity.type'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>Document Type</FormLabel>
                          <Select
                            onValueChange={field.onChange}
                            value={field.value || undefined}
                          >
                            <FormControl>
                              <SelectTrigger>
                                <SelectValue placeholder='Select document type' />
                              </SelectTrigger>
                            </FormControl>
                            <SelectContent>
                              {identityTypes.map((type) => (
                                <SelectItem key={type.type} value={type.type}>
                                  {type.displayName}
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
                          <FormLabel>Document Number</FormLabel>
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
                          <FormLabel>Date of Issue</FormLabel>
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
                          <FormLabel>Expiration Date</FormLabel>
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
                          <FormLabel>Issued By</FormLabel>
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
                              <FormLabel>Has Chip</FormLabel>
                              <p className='text-sm text-muted-foreground'>
                                Specify if this ID card contains a chip
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
                              <FormLabel>Issuing Country</FormLabel>
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
                              <FormLabel>Notes</FormLabel>
                              <FormControl>
                                <textarea
                                  placeholder='Additional information about the passport'
                                  {...field}
                                  className='flex min-h-[80px] w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50'
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
                <FormSection title='Academic Information'>
                  <div className='grid grid-cols-2 gap-4'>
                    <FormField
                      control={form.control}
                      name='faculty'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>Faculty</FormLabel>
                          <Select
                            onValueChange={field.onChange}
                            value={field.value || undefined}
                          >
                            <FormControl>
                              <SelectTrigger className='w-full'>
                                <SelectValue placeholder='Select faculty' />
                              </SelectTrigger>
                            </FormControl>
                            <SelectContent>
                              {facultiesQuery.isLoading ? (
                                <div className='flex items-center justify-center p-2'>
                                  <Loader2 className='h-4 w-4 animate-spin' />
                                </div>
                              ) : facultiesQuery.data ? (
                                facultiesQuery.data.map((faculty) => (
                                  <SelectItem key={faculty} value={faculty}>
                                    {faculty.replace(/_/g, ' ')}
                                  </SelectItem>
                                ))
                              ) : (
                                <SelectItem value='' disabled>
                                  Failed to load faculties
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
                      name='course'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>Course Year</FormLabel>
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
                          <FormLabel>Program</FormLabel>
                          <FormControl>
                            <Select
                              onValueChange={field.onChange}
                              value={field.value || undefined}
                            >
                              <FormControl>
                                <SelectTrigger className='w-full'>
                                  <SelectValue placeholder='Select program' />
                                </SelectTrigger>
                              </FormControl>
                              <SelectContent>
                                {programsQuery.isLoading ? (
                                  <div className='flex items-center justify-center p-2'>
                                    <Loader2 className='h-4 w-4 animate-spin' />
                                  </div>
                                ) : programsQuery.data ? (
                                  programsQuery.data.map((program) => (
                                    <SelectItem key={program} value={program}>
                                      {program.replace(/_/g, ' ')}
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
                      name='status'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>Status</FormLabel>
                          <Select
                            onValueChange={field.onChange}
                            value={field.value || undefined}
                          >
                            <FormControl>
                              <SelectTrigger>
                                <SelectValue placeholder='Select status' />
                              </SelectTrigger>
                            </FormControl>
                            <SelectContent>
                              {statusesQuery.isLoading ? (
                                <div className='flex items-center justify-center p-2'>
                                  <Loader2 className='h-4 w-4 animate-spin' />
                                </div>
                              ) : statusesQuery.data ? (
                                statusesQuery.data.map((status) => (
                                  <SelectItem key={status} value={status}>
                                    {status.replace(/_/g, ' ')}
                                  </SelectItem>
                                ))
                              ) : (
                                <SelectItem value='' disabled>
                                  Failed to load statuses
                                </SelectItem>
                              )}
                            </SelectContent>
                          </Select>
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
                    Cancel
                  </Button>
                  <LoadingButton type='submit' isLoading={isLoading}>
                    {isEditing ? 'Save Changes' : 'Add Student'}
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
