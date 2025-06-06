import { showSuccessToast, showErrorToast } from '@/shared/lib/toast-utils';
import FacultyService from '@faculty/api/facultyService';
import Faculty, {
  CreateFacultyDTO,
  UpdateFacultyDTO,
} from '@faculty/types/faculty';
import { QueryHookParams } from '@/core/types/table';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { getErrorMessage } from '@/shared/lib/utils';
import { useLoadMore } from '@/shared/hooks/useLoadMore';
import { useState } from 'react';
import { SelectItem } from '@/components/common/LoadMoreSelect';
import { useTranslation } from 'react-i18next';
import { t } from 'i18next';

const facultyService = new FacultyService();

export const useFaculties = (params: QueryHookParams) => {
  let { page, pageSize, filters, sort } = params;

  let search = '';
  if (filters.search && typeof filters.search === 'string') {
    search = filters.search;
  }

  // Map sort config to API parameters
  const sortName = sort.key || 'name';
  const sortType = sort.direction || 'asc';

  return useQuery({
    queryKey: ['faculties', page, pageSize, search, sortName, sortType],
    queryFn: () =>
      facultyService.getFaculties({
        page,
        size: pageSize,
        sortName,
        sortType,
        search,
      }),
  });
};

export const useFacultiesDropdown = (
  initialPageSize?: number,
  mapFn?: (arg0: Faculty) => SelectItem,
) => {
  const [facultySearch, setFacultySearch] = useState<string>('');
  const [item, setItem] = useState<SelectItem | undefined>(undefined);
  const faculties = useLoadMore<Faculty>({
    queryKey: ['faculties', 'dropdown'],
    fetchFn: (page, size, search) =>
      facultyService.getFaculties({
        page,
        size,
        sortName: 'name',
        sortType: 'asc',
        search,
      }),
    initialItem: item,
    mapFn:
      mapFn ||
      ((faculty: Faculty) => ({
        id: faculty.id,
        label: faculty.name,
        value: faculty.name,
      })),
    searchQuery: facultySearch,
    initialPageSize,
  });

  return {
    ...faculties,
    setFacultySearch,
    setItem,
  };
};

export const useFaculty = (id: string) => {
  return useQuery({
    queryKey: ['faculty', id],
    queryFn: () => facultyService.getFaculty(id),
    enabled: !!id,
  });
};

export const useCreateFaculty = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: CreateFacultyDTO) => facultyService.addNewFaculty(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['faculties'] });
      showSuccessToast(t('faculty:messages.facultyAdded'));
    },
    onError: (error: any) => {
      showErrorToast(getErrorMessage(error));
    },
  });
};

export const useUpdateFaculty = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, data }: { id: string; data: UpdateFacultyDTO }) =>
      facultyService.updateFaculty(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['faculties'] });
      showSuccessToast(t('faculty:messages.facultyUpdated'));
    },
    onError: (error: any) => {
      showErrorToast(getErrorMessage(error));
    },
  });
};

export const useDeleteFaculty = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (id: string) => facultyService.deleteFaculty(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['faculties'] });
      showSuccessToast(t('faculty:messages.facultyDeleted'));
    },
    onError: (error: any) => {
      showErrorToast(getErrorMessage(error));
    },
  });
};
