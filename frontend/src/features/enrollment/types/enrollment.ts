import Course from '@/features/course/types/course';
import Student from '@/features/student/types/student';

export interface Enrollment {
  id: string;
  student: Partial<Student>;
  course: Partial<Course>;
}

export interface EnrollmentMinimal {
  id: string;
  course: Partial<Course>;
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

export const mapToEnrollment = (data: any): Enrollment => ({
  id: data.id.toString(),
  student: data.student,
  course: data.course,
});

export const mapToEnrollmentMinimal = (data: any): EnrollmentMinimal => ({
  id: data.id.toString(),
  course: data.course,
});

export const mapToEnrollmentHistory = (data: any): EnrollmentHistory => ({
  id: data.id.toString(),
  actionType: data.actionType,
  course: data.course,
  createdAt: new Date(data.createdAt),
});
