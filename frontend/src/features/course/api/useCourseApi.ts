import { showErrorToast, showSuccessToast } from '@/shared/lib/toast-utils';
import CourseService from '@/features/course/api/courseService';
import {
  CreateCourseDTO,
  UpdateCourseDTO,
} from '@/features/course/types/course';
import { QueryHookParams } from '@/core/types/table';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { getErrorMessage } from '@/shared/lib/utils';

const courseService = new CourseService();

export const useCourses = (params: QueryHookParams) => {
  let { page, pageSize, filters, sort } = params;

  let search = '';
  let subject = '';
  let lecturer = '';

  if (filters.search && typeof filters.search === 'string') {
    search = filters.search;
  }

  if (filters.subject && typeof filters.subject === 'string') {
    subject = filters.subject;
  }

  if (filters.lecturer && typeof filters.lecturer === 'string') {
    lecturer = filters.lecturer;
  }

  // Map sort config to API parameters
  const sortName = sort.key || 'code';
  const sortType = sort.direction || 'asc';

  return useQuery({
    queryKey: [
      'courses',
      page,
      pageSize,
      search,
      subject,
      lecturer,
      sortName,
      sortType,
    ],
    queryFn: () =>
      courseService.getCourses({
        page,
        size: pageSize,
        sortName,
        sortType,
        search,
        subject,
        lecturer,
      }),
  });
};

export const useCourse = (id: string) => {
  return useQuery({
    queryKey: ['course', id],
    queryFn: () => courseService.getCourse(id),
    enabled: !!id,
  });
};

export const useCreateCourse = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: CreateCourseDTO) => courseService.addCourse(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['courses'] });
      showSuccessToast('Course created successfully');
    },
    onError: (error) => {
      showErrorToast(getErrorMessage(error));
    },
  });
};

export const useUpdateCourse = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, data }: { id: string; data: UpdateCourseDTO }) =>
      courseService.updateCourse(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['courses'] });
      showSuccessToast('Course updated successfully');
    },
    onError: (error) => {
      showErrorToast(getErrorMessage(error));
    },
  });
};

export const useDeleteCourse = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (id: string) => courseService.deleteCourse(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['courses'] });
      showSuccessToast('Course deleted successfully');
    },
    onError: (error) => {
      showErrorToast(getErrorMessage(error));
    },
  });
};
