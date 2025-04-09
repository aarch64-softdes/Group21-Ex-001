import StatusService from '@status/api/statusService';
import { CreateStatusDTO, UpdateStatusDTO } from '@status/types/status';
import { QueryHookParams } from '@/core/types/table';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { showSuccessToast, showErrorToast } from '@/shared/lib/toast-utils';
import { getErrorMessage } from '@/shared/lib/utils';

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

export const useStatus = (id: string) => {
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
      showSuccessToast('Status created successfully');
    },
    onError: (error) => {
      showErrorToast(getErrorMessage(error));
    },
  });
};

export const useUpdateStatus = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, data }: { id: string; data: UpdateStatusDTO }) =>
      statusService.updateStatus(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['statuses'] });
      showSuccessToast('Status updated successfully');
    },
    onError: (error) => {
      showErrorToast(getErrorMessage(error));
    },
  });
};

export const useDeleteStatus = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (id: string) => statusService.deleteStatus(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['statuses'] });
      showSuccessToast('Status deleted successfully');
    },
    onError: (error) => {
      showErrorToast(getErrorMessage(error));
    },
  });
};
