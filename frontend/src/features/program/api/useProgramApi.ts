import { showErrorToast, showSuccessToast } from '@/shared/lib/toast-utils';
import ProgramService from '@/features/program/api/programService';
import {
  CreateProgramDTO,
  UpdateProgramDTO,
} from '@/features/program/types/program';
import { QueryHookParams } from '@/core/types/table';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { getErrorMessage } from '@/shared/lib/utils';
import Program from '@/features/faculty/types/faculty';
import { useLoadMore } from '@/shared/hooks/useLoadMore';
import { useState } from 'react';
import { SelectItem } from '@/components/common/LoadMoreSelect';
import { useTranslation } from 'react-i18next';
import { t } from 'i18next';

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

export const useProgramsDropdown = (
  initialPageSize?: number,
  mapFn?: (p: Program) => SelectItem,
) => {
  const [programSearch, setProgramSearch] = useState<string>('');
  const programs = useLoadMore<Program>({
    queryKey: ['programs', 'dropdown'],
    fetchFn: (page, size, searchQuery) =>
      programService.getPrograms({
        page,
        size,
        sortName: 'name',
        sortType: 'asc',
        search: searchQuery,
      }),
    mapFn:
      mapFn ||
      ((program: Program) => ({
        id: program.id,
        label: program.name,
        value: program.name,
      })),
    searchQuery: programSearch,
    initialPageSize,
  });

  return {
    ...programs,
    setProgramSearch,
  };
};

export const useProgram = (id: string) => {
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
      showSuccessToast(t('program:messages.programAdded'));
    },
    onError: (error) => {
      showErrorToast(getErrorMessage(error));
    },
  });
};

export const useUpdateProgram = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, data }: { id: string; data: UpdateProgramDTO }) =>
      programService.updateProgram(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['programs'] });
      showSuccessToast(t('program:messages.programUpdated'));
    },
    onError: (error) => {
      showErrorToast(getErrorMessage(error));
    },
  });
};

export const useDeleteProgram = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (id: string) => programService.deleteProgram(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['programs'] });
      showSuccessToast(t('program:messages.programDeleted'));
    },
    onError: (error) => {
      showErrorToast(getErrorMessage(error));
    },
  });
};
