import Subject from '@/features/subject/types/subject';

export default interface Class {
  id: string;
  subjectId: string;
  program: string;
  code: string;
  year: number;
  startAt: Date;
  lecturer: string;
  maxStudent: number;
  schedule: string;
  room: string;
  subject?: Subject;
}

export interface CreateClassDto {
  subjectId: string;
  program: string;
  code: string;
  year: number;
  startAt: Date;
  lecturer: string;
  maxStudent: number;
  schedule: string;
  room: string;
}

export interface UpdateClassDto {
  subjectId?: string;
  program?: string;
  code?: string;
  year?: number;
  startAt?: Date;
  lecturer?: string;
  maxStudent?: number;
  schedule?: string;
  room?: string;
}

export const mapToClass = (data: any): Class => ({
  id: data.id,
  subjectId: data.subjectId,
  program: data.program,
  code: data.code,
  year: data.year,
  startAt: new Date(data.startAt),
  lecturer: data.lecturer,
  maxStudent: data.maxStudent,
  schedule: data.schedule,
  room: data.room,
  subject: data.subject,
});
