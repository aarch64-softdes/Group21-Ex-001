import Program from '@/features/program/types/program';
import Subject from '@/features/subject/types/subject';
import { CourseScheduleDto } from './courseSchedule.ts';

export default interface Course {
  id: string;
  code?: string;
  year: number;
  semester: number;
  lecturer: string;
  maxStudent: number;
  room: string;
  schedule: string;
  startDate: Date;
  subject?: Subject;
  program?: Program;
}

export interface CreateCourseDTO {
  subjectId: string;
  programId: string;
  year: number;
  semester: number;
  lecturer: string;
  maxStudent: number;
  room: string;
  schedule: CourseScheduleDto;
  startDate: Date;
}

export interface UpdateCourseDTO {
  year: number;
  semester: number;
  lecturer: string;
  maxStudent: number;
  room: string;
  schedule: CourseScheduleDto;
  startDate: Date;
}

export const mapToCourse = (data: any): Course => ({
  id: data.id.toString(),
  code: `${data.subject?.code}-${data.id.toString().padStart(2, '0')}`,
  year: data.year,
  semester: data.semester,
  lecturer: data.lecturer,
  maxStudent: data.maxStudent,
  room: data.room,
  schedule: data.schedule,
  startDate: data.startDate ? new Date(data.startDate) : new Date(),
  subject: data.subject,
  program: data.program,
});
