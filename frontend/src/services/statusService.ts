import api from '@/lib/api';
import { ApiResponse } from '@/types/apiResponse';
import Status, {
  CreateStatusDTO,
  UpdateStatusDTO,
  mapToStatus,
} from '@/types/status';

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

    return {
      data: response.data.content.data.map(mapToStatus),
      totalItems: response.data.content.data.length,
      totalPages: 1, // Since pagination is not implemented in these entities
      currentPage: page,
    };
  };

  getStatus = async (id: number): Promise<Status> => {
    const response = await api.get(`/api/statuses/${id}`);

    return mapToStatus(response.data.content);
  };

  addNewStatus = async (data: CreateStatusDTO): Promise<void> => {
    await api.post('/api/statuses', {
      name: data.name,
      validTransitionIds: data.allowedTransitions?.map((t) => t.id) || [],
    });
  };

  updateStatus = async (id: number, data: UpdateStatusDTO): Promise<void> => {
    await api.put(`/api/statuses/${id}`, {
      name: data.name,
      validTransitionIds: data.allowedTransitions?.map((t) => t.id) || [],
    });
  };

  deleteStatus = async (id: number): Promise<void> => {
    await api.delete(`/api/statuses/${id}`);
  };
}
