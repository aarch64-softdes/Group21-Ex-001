import api from '@/core/config/api';
import { ApiResponse } from '@/core/types/apiResponse';
import Status, {
  CreateStatusDTO,
  UpdateStatusDTO,
  mapToStatus,
} from '@/features/status/types/status';

export default class StatusService {
  getStatuses = async ({
    page = 1,
    size = 10,
    sortName = 'name',
    sortType = 'asc',
    search = '',
  }: {
    page?: number;
    size?: number;
    sortName?: string;
    sortType?: string;
    search?: string;
  }): Promise<ApiResponse<Status>> => {
    const response = await api.get('/api/statuses', {
      params: {
        page,
        size,
        sortName,
        sortType,
        search,
      },
    });

    console.log(response.data);

    return {
      data: response.data.content.data.map(mapToStatus),
      totalItems: response.data.content.page.totalItems,
      totalPages: response.data.content.page.totalPages,
      currentPage: response.data.content.page.pageNumber,
    };
  };

  getStatus = async (id: string): Promise<Status> => {
    const response = await api.get(`/api/statuses/${id}`);

    return mapToStatus(response.data.content);
  };

  addNewStatus = async (data: CreateStatusDTO): Promise<void> => {
    await api.post('/api/statuses', {
      name: data.name,
      validTransitionIds: data.allowedTransitions?.map((t) => t.id) || [],
    });
  };

  updateStatus = async (id: string, data: UpdateStatusDTO): Promise<void> => {
    await api.put(`/api/statuses/${id}`, {
      name: data.name,
      validTransitionIds: data.allowedTransitions?.map((t) => t.id) || [],
    });
  };

  deleteStatus = async (id: string): Promise<void> => {
    await api.delete(`/api/statuses/${id}`);
  };
}
