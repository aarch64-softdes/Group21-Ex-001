// Using string types instead of enums since values come from the API
export type Gender = string;
export type Faculty = string;
export type Status = string;

export default interface Student {
  id: string;
  studentId: string;
  name: string;
  dob: Date;
  gender: Gender;
  faculty: Faculty;
  course: number;
  program: string;
  email: string;
  address: string;
  phone: string;
  status: Status;
}

export interface CreateStudentDTO {
  studentId: string;
  name: string;
  dob: Date;
  gender: Gender;
  faculty: Faculty;
  course: number;
  program: string;
  email: string;
  address: string;
  phone: string;
  status?: Status;
}

export interface UpdateStudentDTO {
  name?: string;
  dob?: Date;
  gender?: Gender;
  faculty?: Faculty;
  course?: number;
  program?: string;
  email?: string;
  address?: string;
  phone?: string;
  status?: Status;
}

export const mapToStudent = (data: any): Student => ({
  id: data.id,
  studentId: data.studentId,
  name: data.name,
  dob: data.dob ? new Date(data.dob) : new Date(),
  gender: data.gender,
  faculty: data.faculty,
  course: data.course,
  program: data.program,
  email: data.email,
  address: data.address,
  phone: data.phone,
  status: data.status,
});
