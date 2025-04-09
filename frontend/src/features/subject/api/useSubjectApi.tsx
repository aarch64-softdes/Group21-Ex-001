import SubjectService from '@/features/subject/api/subjectService';
import {
  CreateSubjectDTO,
  UpdateSubjectDTO,
} from '@/features/subject/types/subject';
import { QueryHookParams } from '@/core/types/table';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';

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
        sortName,
        sortType,
        search,
        faculty,
      }),
  });
};

export const useSubject = (id: string) => {
  return useQuery({
    queryKey: ['subject', id],
    queryFn: () => subjectService.getSubject(id),
    enabled: !!id,
  });
};

export const useSubjectsForPrerequisites = (currentId?: string) => {
  return useQuery({
    queryKey: ['subjects-for-prerequisites', currentId],
    queryFn: async () => {
      const response = await subjectService.getSubjects({});
      // Filter out the current subject from the list (can't be its own prerequisite)
      return response.data.filter((subject) => subject.id !== currentId);
    },
  });
};

export const useCreateSubject = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: CreateSubjectDTO) => subjectService.addNewSubject(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['subjects'] });
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
  });
};

export const useDeleteSubject = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (id: string) => subjectService.deleteSubject(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['subjects'] });
    },
  });
};
