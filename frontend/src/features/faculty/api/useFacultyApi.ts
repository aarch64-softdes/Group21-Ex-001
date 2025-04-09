import { showSuccessToast, showErrorToast } from '@/shared/lib/toast-utils';
import FacultyService from '@faculty/api/facultyService';
import { CreateFacultyDTO, UpdateFacultyDTO } from '@faculty/types/faculty';
import { QueryHookParams } from '@/core/types/table';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { getErrorMessage } from '@/shared/lib/utils';

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
      showSuccessToast('Faculty added successfully');
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
      showSuccessToast('Faculty updated successfully');
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
      showSuccessToast('Faculty deleted successfully');
    },
    onError: (error: any) => {
      showErrorToast(getErrorMessage(error));
    },
  });
};
