import api from '@/core/config/api';
import { ApiResponse } from '@/core/types/apiResponse';
import {
  AcademicTranscript,
  CreateEnrollmentDTO,
  DeleteEnrollmentDTO,
  Enrollment,
  EnrollmentHistory,
  EnrollmentMinimal,
  UpdateTranscriptDTO,
  mapToAcademicTranscript,
  mapToEnrollment,
  mapToEnrollmentHistory,
  mapToEnrollmentMinimal,
} from '../types/enrollment';

export default class EnrollmentService {
  getStudentEnrollments = async (
    studentId: string,
    page: number = 1,
    size: number = 10,
  ): Promise<ApiResponse<EnrollmentMinimal>> => {
    const response = await api.get(`/api/enrollments/${studentId}`, {
      params: {
        page,
        size,
      },
    });

    return {
      data: response.data.content.data.map(mapToEnrollmentMinimal),
      totalItems: response.data.content.totalElements,
      totalPages: response.data.content.totalPages,
      currentPage: response.data.content.pageNumber,
    };
  };

  getEnrollmentHistory = async (
    studentId: string,
    page: number = 1,
    size: number = 10,
  ): Promise<ApiResponse<EnrollmentHistory>> => {
    const response = await api.get(`/api/enrollments/${studentId}/history`, {
      params: {
        page,
        size,
      },
    });

    return {
      data: response.data.content.data.map(mapToEnrollmentHistory),
      totalItems: response.data.content.totalElements,
      totalPages: response.data.content.totalPages,
      currentPage: response.data.content.pageNumber,
    };
  };

  getAcademicTranscript = async (
    studentId: string,
  ): Promise<AcademicTranscript> => {
    const response = await api.get(`/api/enrollments/${studentId}/transcript`);
    return mapToAcademicTranscript(response.data.content);
  };

  updateTranscript = async (data: UpdateTranscriptDTO): Promise<Enrollment> => {
    const response = await api.put(
      `/api/enrollments/${data.studentId}/courses/${data.courseId}/transcript`,
      data.transcript,
    );
    return mapToEnrollment(response.data.content);
  };

  enrollCourse = async (data: CreateEnrollmentDTO): Promise<Enrollment> => {
    const response = await api.post('/api/enrollments/enroll', data);
    return mapToEnrollment(response.data.content);
  };

  unenrollCourse = async (data: DeleteEnrollmentDTO): Promise<void> => {
    await api.delete('/api/enrollments/unenroll', { data });
  };
}
