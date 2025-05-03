import StatusService from '@status/api/statusService';
import { CreateStatusDTO, UpdateStatusDTO } from '@status/types/status';
import { QueryHookParams } from '@/core/types/table';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { showSuccessToast, showErrorToast } from '@/shared/lib/toast-utils';
import { getErrorMessage } from '@/shared/lib/utils';
import Status from '@/features/faculty/types/faculty';
import { useLoadMore } from '@/shared/hooks/useLoadMore';
import { useState } from 'react';
import { t } from 'i18next';
import { SelectItem } from '@/components/common/LoadMoreSelect';

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

export const useStatusesDropdown = (
  initialPageSize?: number,
  mapFn?: (status: Status) => SelectItem,
) => {
  const [statusSearch, setStatusSearch] = useState<string>('');

  const statuses = useLoadMore<Status>({
    queryKey: ['statuses', 'dropdown'],
    fetchFn: (page, size, searchQuery) =>
      statusService.getStatuses({
        page,
        size,
        sortName: 'name',
        sortType: 'asc',
        search: searchQuery,
      }),
    mapFn:
      mapFn ||
      ((status: Status) => ({
        id: status.id,
        label: status.name,
        value: status.name,
      })),
    searchQuery: statusSearch,
    initialPageSize,
  });

  return {
    ...statuses,
    setStatusSearch,
  };
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
      showSuccessToast(t('status:messages.statusAdded'));
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
      showSuccessToast(t('status:messages.statusUpdated'));
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
      showSuccessToast(t('status:messages.statusDeleted'));
    },
    onError: (error) => {
      showErrorToast(getErrorMessage(error));
    },
  });
};
