import { showSuccessToast } from '@/lib/toast-utils';
import FacultyService from '@/services/facultyService';
import { CreateFacultyDTO, UpdateFacultyDTO } from '@/types/faculty';
import { QueryHookParams } from '@/types/table';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';

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

export const useFaculty = (id: number) => {
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
  });
};

export const useUpdateFaculty = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, data }: { id: number; data: UpdateFacultyDTO }) =>
      facultyService.updateFaculty(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['faculties'] });
      showSuccessToast('Faculty updated successfully');
    },
  });
};

export const useDeleteFaculty = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (id: number) => facultyService.deleteFaculty(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['faculties'] });
      showSuccessToast('Faculty deleted successfully');
    },
  });
};
