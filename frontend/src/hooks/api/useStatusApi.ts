import { showSuccessToast } from '@/lib/toast-utils';
import StatusService from '@/services/statusService';
import { CreateStatusDTO, UpdateStatusDTO } from '@/types/status';
import { QueryHookParams } from '@/types/table';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';

const statusService = new StatusService();

export const useStatuses = (params: QueryHookParams) => {
  let { page, pageSize, filters, sort } = params;

  let search = '';
  if (filters.search && typeof filters.search === 'string') {
    search = filters.search;
  }

  // Map sort config to API parameters
  const sortName = sort.key || 'name';
  const sortType = sort.direction || 'asc';

  return useQuery({
    queryKey: ['statuses', page, pageSize, search, sortName, sortType],
    queryFn: () =>
      statusService.getStatuses({
        page,
        size: pageSize,
        sortName,
        sortType,
        search,
      }),
  });
};

export const useStatus = (id: number) => {
  return useQuery({
    queryKey: ['status', id],
    queryFn: () => statusService.getStatus(id),
    enabled: !!id,
  });
};

export const useCreateStatus = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: CreateStatusDTO) => statusService.addNewStatus(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['statuses'] });
    },
  });
};

export const useUpdateStatus = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, data }: { id: number; data: UpdateStatusDTO }) =>
      statusService.updateStatus(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['statuses'] });
    },
  });
};

export const useDeleteStatus = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (id: number) => statusService.deleteStatus(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['statuses'] });
    },
  });
};
