// src/features/enrollment/api/useEnrollmentApi.ts
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import EnrollmentService from './enrollmentService';
import {
  CreateEnrollmentDTO,
  DeleteEnrollmentDTO,
  UpdateTranscriptDTO,
} from '../types/enrollment';
import { showErrorToast, showSuccessToast } from '@/shared/lib/toast-utils';
import { getErrorMessage } from '@/shared/lib/utils';

const enrollmentService = new EnrollmentService();

export const useEnrollments = (
  studentId: string,
  page: number = 1,
  pageSize: number = 10,
) => {
  return useQuery({
    queryKey: ['enrollments', studentId, page, pageSize],
    queryFn: () =>
      enrollmentService.getStudentEnrollments(studentId, page, pageSize),
    enabled: !!studentId,
  });
};

export const useEnrollmentHistory = (
  studentId: string,
  page: number = 1,
  pageSize: number = 10,
) => {
  return useQuery({
    queryKey: ['enrollmentHistory', studentId, page, pageSize],
    queryFn: () =>
      enrollmentService.getEnrollmentHistory(studentId, page, pageSize),
    enabled: !!studentId,
  });
};

export const useAcademicTranscript = (studentId: string) => {
  return useQuery({
    queryKey: ['academicTranscript', studentId],
    queryFn: () => enrollmentService.getAcademicTranscript(studentId),
    enabled: !!studentId,
  });
};

export const useUpdateTranscript = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: UpdateTranscriptDTO) =>
      enrollmentService.updateTranscript(data),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ['enrollments'],
      });
      queryClient.invalidateQueries({
        queryKey: ['academicTranscript'],
      });
      showSuccessToast('Successfully updated transcript');
    },
    onError: (error) => {
      showErrorToast(getErrorMessage(error));
    },
  });
};

export const useEnrollCourse = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: CreateEnrollmentDTO) =>
      enrollmentService.enrollCourse(data),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ['enrollments'],
      });
      queryClient.invalidateQueries({
        queryKey: ['enrollmentHistory'],
      });
      showSuccessToast('Successfully enrolled in course');
    },
    onError: (error) => {
      showErrorToast(getErrorMessage(error));
    },
  });
};

export const useUnenrollCourse = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: DeleteEnrollmentDTO) =>
      enrollmentService.unenrollCourse(data),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ['enrollments'],
      });
      queryClient.invalidateQueries({
        queryKey: ['enrollmentHistory'],
      });
      showSuccessToast('Successfully unenrolled from course');
    },
    onError: (error) => {
      showErrorToast(getErrorMessage(error));
    },
  });
};
