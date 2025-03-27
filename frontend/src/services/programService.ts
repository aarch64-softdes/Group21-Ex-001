import api from '@/lib/api';
import { ApiResponse } from '@/types/apiResponse';
import Program, {
  CreateProgramDTO,
  UpdateProgramDTO,
  mapToProgram,
} from '@/types/program';

export default class ProgramService {
  getPrograms = async ({
    page = 1,
    size = 20,
    sortName = 'name',
    sortType = 'asc',
    search = '',
  }: {
    page?: number;
    size?: number;
    sortName?: string;
    sortType?: string;
    search?: string;
  }): Promise<ApiResponse<Program>> => {
    const response = await api.get('/api/programs', {
      params: {
        page,
        size,
        sortName,
        sortType,
        search,
      },
    });

    return {
      data: response.data.content.data.map(mapToProgram),
      totalItems: response.data.content.data.length,
      totalPages: 1, // Since pagination is not implemented in these entities
      currentPage: page,
    };
  };

  getProgram = async (id: number): Promise<Program> => {
    const response = await api.get(`/api/programs/${id}`);
    return mapToProgram(response.data.content);
  };

  addNewProgram = async (data: CreateProgramDTO): Promise<void> => {
    await api.post('/api/programs', data);
  };

  updateProgram = async (id: number, data: UpdateProgramDTO): Promise<void> => {
    await api.put(`/api/programs/${id}`, data);
  };

  deleteProgram = async (id: number): Promise<void> => {
    await api.delete(`/api/programs/${id}`);
  };
}
