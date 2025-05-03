import { Button } from '@/components/ui/button';
import { DetailComponentProps } from '@/core/types/table';
import { useStudent } from '@/features/student/api/useStudentApi';
import { Country, findCountryByCode } from '@/shared/data/countryData';
import { Avatar, AvatarFallback } from '@ui/avatar';
import { Badge } from '@ui/badge';
import { Card, CardContent, CardHeader, CardTitle } from '@ui/card';
import { Separator } from '@ui/separator';
import { t } from 'i18next';
import {
  CornerUpRight,
  Loader2,
  Mail,
  Phone,
  School,
  User,
} from 'lucide-react';
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

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
  const navigate = useNavigate();

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
        <p className='text-muted-foreground'>{t('student:studentNotFound')}</p>
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
          <div className='flex flex-col md:flex-row md:items-center justify-between '>
            <div className='space-y-0.5'>
              <h1 className='text-2xl font-bold'>{student.name}</h1>
              <p className='text-muted-foreground text-sm'>
                {student.studentId}
              </p>
              <div className='flex items-center gap-1.5 text-sm text-muted-foreground'>
                <Mail className='h-4 w-4' />
                <span>{student.email}</span>
              </div>
              <div className='flex items-center gap-1.5 text-sm text-muted-foreground'>
                <Phone className='h-4 w-4' />
                <span className='mr-1'>
                  {country.flag} {student.phone.phoneNumber}
                </span>
              </div>
            </div>
            <div className='flex flex-col gap-3 items-end'>
              <Badge
                className={`text-xs px-3 py-1 mt-2 md:mt-0 ${getStatusColor(
                  student.status,
                )}`}
              >
                {student.status}
              </Badge>
              <Button
                variant='outline'
                className='mt-2 md:mt-0'
                onClick={() => navigate(`/student/${studentId}/enrollments`)}
              >
                {t('student:enrollments')} {student.name}
                <CornerUpRight className='ml-2 h-4 w-4' />
              </Button>
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
              {t('student:sections.basicInfo')}
            </CardTitle>
            <Separator />
          </CardHeader>
          <CardContent className='grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4'>
            <DetailField
              label={t('student:fields.gender')}
              value={student.gender}
            />
            <DetailField
              label={t('student:fields.dob')}
              value={formatDate(student.dob)}
            />
            <DetailField
              label={t('student:fields.address.permanent')}
              colSpan='full'
            >
              {student.permanentAddress
                ? [
                    student.permanentAddress.street,
                    student.permanentAddress.ward,
                    student.permanentAddress.district,
                    student.permanentAddress.province,
                    student.permanentAddress.country,
                  ]
                    .filter(Boolean)
                    .join(', ') || t('student:noPermanentAddress')
                : t('student:noPermanentAddress')}
            </DetailField>
            <DetailField
              label={t('student:fields.address.temporary')}
              colSpan='full'
            >
              {student.temporaryAddress
                ? [
                    student.temporaryAddress.street,
                    student.temporaryAddress.ward,
                    student.temporaryAddress.district,
                    student.temporaryAddress.province,
                    student.temporaryAddress.country,
                  ]
                    .filter(Boolean)
                    .join(', ') || t('student:noTemporaryAddress')
                : t('student:noTemporaryAddress')}
            </DetailField>
            <DetailField
              label={t('student:fields.address.mailing')}
              colSpan='full'
            >
              {student.mailingAddress
                ? [
                    student.mailingAddress.street,
                    student.mailingAddress.ward,
                    student.mailingAddress.district,
                    student.mailingAddress.province,
                    student.mailingAddress.country,
                  ]
                    .filter(Boolean)
                    .join(', ') || t('student:noMailingAddress')
                : t('student:noMailingAddress')}
            </DetailField>
            <Separator className='col-span-3' />

            {student.identity && student.identity.type && (
              <>
                <DetailField
                  label={t('student:fields.identity.type')}
                  value={student.identity.type}
                />
                <DetailField
                  label={t('student:fields.identity.number')}
                  value={student.identity.number}
                />
                <DetailField
                  label={t('student:fields.identity.issueDate')}
                  value={
                    student.identity.issuedDate
                      ? formatDate(student.identity.issuedDate)
                      : 'N/A'
                  }
                />
                <DetailField
                  label={t('student:fields.identity.issuedBy')}
                  value={student.identity.issuedBy || 'N/A'}
                />
                <DetailField
                  label={t('student:fields.identity.expiryDate')}
                  value={
                    student.identity.expiryDate
                      ? formatDate(student.identity.expiryDate)
                      : 'N/A'
                  }
                />

                {student.identity.type.toLowerCase() === 'chip card' && (
                  <DetailField
                    label={t('student:fields.identity.hasChip')}
                    value={
                      student.identity.hasChip
                        ? t('common:yes')
                        : t('common:no')
                    }
                  />
                )}

                {student.identity.type.toLowerCase() === 'passport' && (
                  <DetailField
                    label={t('student:fields.identity.country')}
                    value={student.identity.country || 'N/A'}
                  />
                )}

                {student.identity.type.toLowerCase() === 'passport' &&
                  student.identity.notes && (
                    <DetailField
                      label={t('common:notes')}
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
              {t('student:sections.academicInfo')}
            </CardTitle>
            <Separator />
          </CardHeader>
          <CardContent className='grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4'>
            <div className='flex flex-col'>
              <dt className='text-sm font-medium text-muted-foreground'>
                {t('student:fields.faculty')}
              </dt>
              <dd className='mt-1'>{student.faculty}</dd>
            </div>
            <div className='flex flex-col'>
              <dt className='text-sm font-medium text-muted-foreground'>
                {t('student:fields.program')}
              </dt>
              <dd className='mt-1'>{student.program || 'N/A'}</dd>
            </div>
            <div className='flex flex-col'>
              <dt className='text-sm font-medium text-muted-foreground'>
                {t('student:fields.schoolYear')}
              </dt>
              <dd className='mt-1'>{student.schoolYear}</dd>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
};

export default StudentDetail;
