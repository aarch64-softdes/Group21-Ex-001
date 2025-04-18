import Course, { mapToCourse } from '@/features/course/types/course';
import Student from '@/features/student/types/student';

export interface Score {
  grade: string;
  gpa: number;
}

export interface Transcript {
  subjectCode: string;
  subjectName: string;
  grade: string;
  gpa: number;
}

export interface AcademicTranscript {
  studentId: string;
  studentName: string;
  courseName: string;
  studentDob: Date;
  gpa: number;
  transcriptList: Transcript[];
}

export interface Enrollment {
  id: string;
  student: Partial<Student>;
  course: Partial<Course>;
  score?: Score;
}

export interface EnrollmentMinimal {
  id: string;
  course: Partial<Course>;
  score?: Score;
}

export interface EnrollmentHistory {
  id: string;
  actionType: string;
  course: Partial<Course>;
  createdAt: Date;
}

export interface CreateEnrollmentDTO {
  studentId: string;
  courseId: string;
}

export interface DeleteEnrollmentDTO {
  studentId: string;
  courseId: string;
}

export interface UpdateTranscriptDTO {
  studentId: string;
  courseId: string;
  transcript: {
    grade: string;
    gpa: number;
  };
}

export const mapToEnrollment = (data: any): Enrollment => ({
  id: data.id.toString(),
  student: data.student,
  course: data.course,
  score: data.score
    ? {
        grade: data.score.grade,
        gpa: data.score.gpa,
      }
    : undefined,
});

export const mapToEnrollmentMinimal = (data: any): EnrollmentMinimal => ({
  id: data.id.toString(),
  course: mapToCourse(data.course),
  score: data.score
    ? {
        grade: data.score.grade,
        gpa: data.score.gpa,
      }
    : undefined,
});

export const mapToEnrollmentHistory = (data: any): EnrollmentHistory => ({
  id: data.id.toString(),
  actionType: data.actionType,
  course: data.course,
  createdAt: new Date(data.createdAt),
});

export const mapToAcademicTranscript = (data: any): AcademicTranscript => ({
  studentId: data.studentId,
  studentName: data.studentName,
  courseName: data.courseName,
  studentDob: new Date(data.studentDob),
  gpa: data.gpa,
  transcriptList: data.transcriptList.map((item: any) => ({
    subjectCode: item.subjectCode,
    subjectName: item.subjectName,
    grade: item.grade,
    gpa: item.gpa,
  })),
});
