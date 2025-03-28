import React, { useEffect, useState } from 'react';
import { DetailComponentProps } from '@/types/table';
import { useStudent } from '@/hooks/api/useStudentApi';
import { Loader2, User, Mail, Phone, School, Calendar } from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Separator } from '@/components/ui/separator';
import { Avatar, AvatarFallback } from '@/components/ui/avatar';
import { Country, findCountryByCode } from '@/data/countryData';

interface DetailFieldProps {
  label: string;
  value?: React.ReactNode;
  children?: React.ReactNode;
  colSpan?: 'default' | 'full';
}

const DetailField: React.FC<DetailFieldProps> = ({
  label,
  value,
  children,
  colSpan = 'default',
}) => {
  return (
    <div className={`flex flex-col ${colSpan === 'full' ? 'col-span-3' : ''}`}>
      <dt className='text-sm font-medium text-muted-foreground'>{label}</dt>
      <dd className='mt-1'>{children || value || 'N/A'}</dd>
    </div>
  );
};

const StudentDetail: React.FC<DetailComponentProps> = ({ id: studentId }) => {
  const { data: student, isLoading } = useStudent(studentId as string);

  let [country, setCountry] = useState<Country>({
    name: '',
    code: '',
    dialCode: '',
    flag: '',
  });

  useEffect(() => {
    if (student) {
      setCountry(findCountryByCode(student.phone.countryCode));
    }
  }, [student]);

  if (isLoading) {
    return (
      <div className='flex items-center justify-center h-48'>
        <Loader2 className='h-8 w-8 animate-spin text-primary' />
      </div>
    );
  }

  if (!student) {
    return (
      <div className='flex items-center justify-center h-48'>
        <p className='text-muted-foreground'>Student not found</p>
      </div>
    );
  }

  const formatDate = (dateString: string | Date) => {
    const date = new Date(dateString);
    return date.toLocaleDateString();
  };

  // Get initials for avatar
  const getInitials = (name: string) => {
    return name
      .split(' ')
      .map((part) => part[0])
      .join('')
      .toUpperCase()
      .substring(0, 2);
  };

  const getStatusColor = (status: string) => {
    switch (status.toLowerCase()) {
      case 'studying':
        return 'bg-green-100 text-green-800';
      case 'graduated':
        return 'bg-blue-100 text-blue-800';
      case 'suspended':
        return 'bg-amber-100 text-amber-800';
      case 'expelled':
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  return (
    <div className='container mx-auto p-6 max-w-4xl'>
      {/* Header with student basic info */}
      <div className='flex flex-col md:flex-row items-start md:items-center gap-6 mb-8'>
        <Avatar className='h-24 w-24 border-4 border-background'>
          <AvatarFallback className='text-2xl bg-primary/10 text-primary'>
            {getInitials(student.name)}
          </AvatarFallback>
        </Avatar>

        <div className='flex-1'>
          <div className='flex flex-col md:flex-row md:items-center justify-between'>
            <div>
              <h1 className='text-2xl font-bold'>{student.name}</h1>
              <p className='text-muted-foreground text-sm'>
                {student.studentId}
              </p>
            </div>
            <Badge
              className={`text-xs px-3 py-1 mt-2 md:mt-0 ${getStatusColor(
                student.status,
              )}`}
            >
              {student.status}
            </Badge>
          </div>

          <div className='grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-2 mt-4'>
            <div className='flex items-center gap-2 text-sm text-muted-foreground'>
              <Mail className='h-4 w-4' />
              <span>{student.email}</span>
            </div>
            <div className='flex items-center gap-2 text-sm text-muted-foreground'>
              <Phone className='h-4 w-4' />
              <span className='mr-1'>
                {country.flag} {student.phone.phoneNumber}
              </span>
            </div>
            <div className='flex items-center gap-2 text-sm text-muted-foreground'>
              <Calendar className='h-4 w-4' />
              <span>DOB: {formatDate(student.dob)}</span>
            </div>
          </div>
        </div>
      </div>

      <div className='space-y-6'>
        {/* Personal Information */}
        <Card>
          <CardHeader className=''>
            <CardTitle className='text-lg flex items-center gap-2'>
              <User className='h-5 w-5' />
              Personal Information
            </CardTitle>
            <Separator />
          </CardHeader>
          <CardContent className='grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4'>
            <DetailField label='Gender' value={student.gender} />
            <DetailField label='Permanent Address' colSpan='full'>
              {student.permanentAddress
                ? [
                    student.permanentAddress.street,
                    student.permanentAddress.ward,
                    student.permanentAddress.district,
                    student.permanentAddress.province,
                    student.permanentAddress.country,
                  ]
                    .filter(Boolean)
                    .join(', ') || 'No permanent address provided'
                : 'No permanent address provided'}
            </DetailField>
            <DetailField label='Temporary Address' colSpan='full'>
              {student.temporaryAddress
                ? [
                    student.temporaryAddress.street,
                    student.temporaryAddress.ward,
                    student.temporaryAddress.district,
                    student.temporaryAddress.province,
                    student.temporaryAddress.country,
                  ]
                    .filter(Boolean)
                    .join(', ') || 'No temporary address provided'
                : 'No temporary address provided'}
            </DetailField>
            <DetailField label='Mailing Address' colSpan='full'>
              {student.mailingAddress
                ? [
                    student.mailingAddress.street,
                    student.mailingAddress.ward,
                    student.mailingAddress.district,
                    student.mailingAddress.province,
                    student.mailingAddress.country,
                  ]
                    .filter(Boolean)
                    .join(', ') || 'No mailing address provided'
                : 'No mailing address provided'}
            </DetailField>
            <Separator className='col-span-3' />

            {student.identity && student.identity.type && (
              <>
                <DetailField
                  label='Identity Document Type'
                  value={student.identity.type}
                />
                <DetailField
                  label='Document Number'
                  value={student.identity.number}
                />
                <DetailField
                  label='Issue Date'
                  value={
                    student.identity.issuedDate
                      ? formatDate(student.identity.issuedDate)
                      : 'N/A'
                  }
                />
                <DetailField
                  label='Issue Place'
                  value={student.identity.issuedBy || 'N/A'}
                />
                <DetailField
                  label='Expiry Date'
                  value={
                    student.identity.expiryDate
                      ? formatDate(student.identity.expiryDate)
                      : 'N/A'
                  }
                />

                {student.identity.type.toLowerCase() === 'chip card' && (
                  <DetailField
                    label='Has Chip'
                    value={student.identity.hasChip ? 'Yes' : 'No'}
                  />
                )}

                {student.identity.type.toLowerCase() === 'passport' && (
                  <DetailField
                    label='Issued Country'
                    value={student.identity.country || 'N/A'}
                  />
                )}

                {student.identity.type.toLowerCase() === 'passport' &&
                  student.identity.notes && (
                    <DetailField
                      label='Note'
                      value={student.identity.notes}
                      colSpan='full'
                    />
                  )}
              </>
            )}
          </CardContent>
        </Card>

        {/* Academic Information */}
        <Card>
          <CardHeader className=''>
            <CardTitle className='text-lg flex items-center gap-2'>
              <School className='h-5 w-5' />
              Academic Information
            </CardTitle>
            <Separator />
          </CardHeader>
          <CardContent className='grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4'>
            <div className='flex flex-col'>
              <dt className='text-sm font-medium text-muted-foreground'>
                Faculty
              </dt>
              <dd className='mt-1'>{student.faculty}</dd>
            </div>
            <div className='flex flex-col'>
              <dt className='text-sm font-medium text-muted-foreground'>
                Program
              </dt>
              <dd className='mt-1'>{student.program || 'N/A'}</dd>
            </div>
            <div className='flex flex-col'>
              <dt className='text-sm font-medium text-muted-foreground'>
                Course Year
              </dt>
              <dd className='mt-1'>{student.course}</dd>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
};

export default StudentDetail;
