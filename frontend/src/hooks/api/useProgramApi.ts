import { showSuccessToast } from '@/lib/toast-utils';
import ProgramService from '@/services/programService';
import { CreateProgramDTO, UpdateProgramDTO } from '@/types/program';
import { QueryHookParams } from '@/types/table';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';

const programService = new ProgramService();

export const usePrograms = (params: QueryHookParams) => {
  let { page, pageSize, filters, sort } = params;

  let search = '';
  if (filters.search && typeof filters.search === 'string') {
    search = filters.search;
  }

  // Map sort config to API parameters
  const sortName = sort.key || 'name';
  const sortType = sort.direction || 'asc';

  return useQuery({
    queryKey: ['programs', page, pageSize, search, sortName, sortType],
    queryFn: () =>
      programService.getPrograms({
        page,
        size: pageSize,
        sortName,
        sortType,
        search,
      }),
  });
};

export const useProgram = (id: number) => {
  return useQuery({
    queryKey: ['program', id],
    queryFn: () => programService.getProgram(id),
    enabled: !!id,
  });
};

export const useCreateProgram = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: CreateProgramDTO) => programService.addNewProgram(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['programs'] });
      showSuccessToast('Program created successfully');
    },
  });
};

export const useUpdateProgram = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, data }: { id: number; data: UpdateProgramDTO }) =>
      programService.updateProgram(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['programs'] });
      showSuccessToast('Program updated successfully');
    },
  });
};

export const useDeleteProgram = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (id: number) => programService.deleteProgram(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['programs'] });
      showSuccessToast('Program deleted successfully');
    },
  });
};
