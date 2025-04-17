import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import EnrollmentService from './enrollmentService';
import { CreateEnrollmentDTO, DeleteEnrollmentDTO } from '../types/enrollment';
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

export const useEnrollCourse = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: CreateEnrollmentDTO) =>
      enrollmentService.enrollCourse(data),
    onSuccess: () => {
      queryClient.invalidateQueries();
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
      queryClient.invalidateQueries();
      showSuccessToast('Successfully unenrolled from course');
    },
    onError: (error) => {
      showErrorToast(getErrorMessage(error));
    },
  });
};
