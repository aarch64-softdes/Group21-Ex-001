import { Address } from '@student/types/address';
import IdentityDocument from '@student/types/identityDocument';

export type Gender = string;

export default interface Student {
  id: string;
  studentId: string;
  name: string;
  dob: Date;
  gender: Gender;
  faculty: string;
  facultyId?: string;
  schoolYear: number;
  program: string;
  programId?: string;
  email: string;
  permanentAddress: Address;
  temporaryAddress: Address;
  mailingAddress: Address;
  phone: Phone;
  status: string;
  statusId?: string;
  identity: IdentityDocument;
}

export interface CreateStudentDTO {
  studentId: string;
  name: string;
  dob: Date;
  gender: Gender;
  facultyId: string;
  schoolYear: number;
  programId: string;
  email: string;
  permanentAddress: Address;
  temporaryAddress: Address;
  mailingAddress: Address;
  phone: Phone;
  statusId: string;
  identity: IdentityDocument;
}

export interface UpdateStudentDTO {
  name?: string;
  dob?: Date;
  gender?: Gender;
  facultyId?: string;
  schoolYear?: number;
  programId?: string;
  email?: string;
  permanentAddress: Address;
  temporaryAddress: Address;
  mailingAddress: Address;
  phone?: Phone;
  statusId?: string;
  identity: IdentityDocument;
}

export interface Phone {
  phoneNumber: string;
  countryCode: string;
}

export const mapToStudent = (data: any): Student => {
  const res = {
    id: data.id.toString(),
    studentId: data.studentId.toString(),
    name: data.name,
    dob: data.dob ? new Date(data.dob) : new Date(),
    gender: data.gender,
    faculty: data.faculty?.name || data.faculty, // Handle both object and string
    facultyId: data.faculty?.id?.toString() || undefined, // Extract ID as string if available
    schoolYear: data.schoolYear,
    program: data.program?.name || data.program, // Handle both object and string
    programId: data.program?.id?.toString() || undefined, // Extract ID as string if available
    email: data.email,
    permanentAddress: data.permanentAddress,
    temporaryAddress: data.temporaryAddress,
    mailingAddress: data.mailingAddress,
    phone: data.phone,
    status: data.status?.name || data.status, // Handle both object and string
    statusId: data.status?.id?.toString() || undefined, // Extract ID as string if available
    identity: data.identity,
  };

  return res;
};
