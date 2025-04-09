import Subject from '@/features/subject/types/subject';

export default interface Class {
  id: string;
  subjectId: string;
  programId: string;
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
  programId: string;
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
  programId?: string;
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
  programId: data.programId,
  code: data.code,
  year: data.year,
  startAt: new Date(data.startAt),
  lecturer: data.lecturer,
  maxStudent: data.maxStudent,
  schedule: data.schedule,
  room: data.room,
  subject: data.subject,
});
