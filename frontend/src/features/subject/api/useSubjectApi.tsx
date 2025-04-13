import SubjectService from '@/features/subject/api/subjectService';
import Subject, {
  CreateSubjectDTO,
  UpdateSubjectDTO,
} from '@/features/subject/types/subject';
import { QueryHookParams } from '@/core/types/table';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { showErrorToast } from '@/shared/lib/toast-utils';
import { getErrorMessage } from '@/shared/lib/utils';
import { useLoadMore } from '@/shared/hooks/useLoadMore';
import { useState } from 'react';

const subjectService = new SubjectService();

export const useSubjects = (params: QueryHookParams) => {
  let { page, pageSize, filters, sort } = params;

  let search = '';
  let faculty = '';

  if (filters.search && typeof filters.search === 'string') {
    search = filters.search;
  }

  if (filters.faculty && typeof filters.faculty === 'string') {
    faculty = filters.faculty;
  }

  // Map sort config to API parameters
  const sortName = sort.key || 'name';
  const sortType = sort.direction || 'asc';

  return useQuery({
    queryKey: ['subjects', page, pageSize, search, faculty, sortName, sortType],
    queryFn: () =>
      subjectService.getSubjects({
        page,
        size: pageSize,
        sortBy: sortName,
        sortDirection: sortType,
        search,
        faculty,
      }),
  });
};

export const useSubjectsDropdown = (
  initialPageSize?: number,
  excludeId?: string,
) => {
  const [subjectSearch, setSubjectSearch] = useState<string>('');
  const subjects = useLoadMore<Subject>({
    queryKey: ['subjects', 'dropdown'],
    fetchFn: async (page, size, searchQuery) => {
      let { data, totalItems } = await subjectService.getSubjects({
        page,
        size,
        sortBy: 'name',
        sortDirection: 'asc',
        search: searchQuery,
      });
      if (excludeId) {
        data = data.filter((subject) => subject.id !== excludeId);
        totalItems = data.length;
      }
      return {
        data,
        totalItems,
      };
    },
    mapFn: (subject: Subject) => ({
      id: subject.id,
      label: subject.name,
      value: subject.id,
    }),
    searchQuery: subjectSearch,
    initialPageSize,
  });

  return {
    ...subjects,
    setSubjectSearch,
  };
};

export const useSubject = (id: string) => {
  return useQuery({
    queryKey: ['subject', id],
    queryFn: () => subjectService.getSubject(id),
    enabled: !!id,
  });
};

export const useCreateSubject = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: CreateSubjectDTO) => subjectService.addNewSubject(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['subjects'] });
    },
    onError: (error) => {
      showErrorToast(getErrorMessage(error));
    },
  });
};

export const useUpdateSubject = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, data }: { id: string; data: UpdateSubjectDTO }) =>
      subjectService.updateSubject(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['subjects'] });
    },
    onError: (error) => {
      showErrorToast(getErrorMessage(error));
    },
  });
};

export const useDeleteSubject = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (id: string) => subjectService.deleteSubject(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['subjects'] });
    },
    onError: (error) => {
      showErrorToast(getErrorMessage(error));
    },
  });
};
