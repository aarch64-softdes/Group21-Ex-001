import Program from '@/features/program/types/program';
import Subject from '@/features/subject/types/subject';
import { CourseScheduleDto, formatSchedule } from './courseSchedule.ts';

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
  subject?: Partial<Subject>;
  program?: Partial<Program>;
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
  code: data.code,
  year: data.year,
  semester: data.semester,
  lecturer: data.lecturer,
  maxStudent: data.maxStudent,
  room: data.room,
  schedule: formatSchedule(data.schedule),
  startDate: data.startDate ? new Date(data.startDate) : new Date(),
  subject:
    typeof data.subject === 'string'
      ? {
          name: data.subject,
          code: data.subjectCode,
        }
      : {
          id: data.subject.id.toString(),
          name: data.subject.name,
          code: data.subject.code,
        },
  program: data.program
    ? typeof data.program === 'string'
      ? {
          name: data.program,
        }
      : {
          id: data.program.id.toString(),
          name: data.program.name,
        }
    : {
        id: '',
        name: '',
      },
});
