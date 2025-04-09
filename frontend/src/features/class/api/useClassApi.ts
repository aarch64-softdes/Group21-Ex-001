import { showSuccessToast } from '@/shared/lib/toast-utils';
import ClassService from '@/features/class/api/classService';
import { CreateClassDto, UpdateClassDto } from '@/features/class/types/class';
import { QueryHookParams } from '@/core/types/table';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';

const classService = new ClassService();

export const useClasses = (params: QueryHookParams) => {
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
      'classes',
      page,
      pageSize,
      search,
      subject,
      lecturer,
      sortName,
      sortType,
    ],
    queryFn: () =>
      classService.getClasses({
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

export const useClass = (id: string) => {
  return useQuery({
    queryKey: ['class', id],
    queryFn: () => classService.getClass(id),
    enabled: !!id,
  });
};

export const useSubjectsForDropdown = () => {
  return useQuery({
    queryKey: ['subjects-dropdown'],
    queryFn: () => classService.getSubjects(),
  });
};

export const useCreateClass = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: CreateClassDto) => classService.addClass(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['classes'] });
      showSuccessToast('Class created successfully');
    },
  });
};

export const useUpdateClass = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, data }: { id: string; data: UpdateClassDto }) =>
      classService.updateClass(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['classes'] });
      showSuccessToast('Class updated successfully');
    },
  });
};

export const useDeleteClass = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (id: string) => classService.deleteClass(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['classes'] });
      showSuccessToast('Class deleted successfully');
    },
  });
};
