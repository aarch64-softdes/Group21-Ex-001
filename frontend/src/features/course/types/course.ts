import Program from '@/features/program/types/program';
import Subject from '@/features/subject/types/subject';

export default interface Course {
  id: string;
  subjectId: string;
  code: string;
  year: number;
  startAt: Date;
  lecturer: string;
  maxStudent: number;
  schedule: string;
  room: string;
  subject?: Subject;
  program?: Program | string;
}

export interface CreateCourseDTO {
  subjectId: string;
  programId: string;
  code: string;
  year: number;
  startAt: Date;
  lecturer: string;
  maxStudent: number;
  schedule: string;
  room: string;
}

export interface UpdateCourseDTO {
  subjectId?: string;
  programId?: string;
  code?: string;
  year?: number;
  startAt?: Date;
  lecturer?: string;
  maxStudent?: number;
  schedule?: string;
  room?: string;
}

export const mapToCourse = (data: any): Course => ({
  id: data.id as string,
  subjectId: data.subjectId as string,
  code: data.code,
  year: data.year,
  startAt: new Date(data.startAt),
  lecturer: data.lecturer,
  maxStudent: data.maxStudent,
  schedule: data.schedule,
  room: data.room,
  subject: data.subject,
  program: data.program,
});
