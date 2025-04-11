import api from '@/core/config/api';
import { ApiResponse } from '@/core/types/apiResponse';
import Program, {
  CreateProgramDTO,
  UpdateProgramDTO,
  mapToProgram,
} from '@program/types/program';

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

    console.log('response', response.data);

    return {
      data: response.data.content.data.map(mapToProgram),
      totalItems: response.data.content.page.totalElements,
      totalPages: response.data.content.page.totalPages,
      currentPage: response.data.content.page.pageNumber,
    };
  };

  getProgram = async (id: string): Promise<Program> => {
    const response = await api.get(`/api/programs/${id}`);
    return mapToProgram(response.data.content);
  };

  addNewProgram = async (data: CreateProgramDTO): Promise<void> => {
    await api.post('/api/programs', data);
  };

  updateProgram = async (id: string, data: UpdateProgramDTO): Promise<void> => {
    await api.put(`/api/programs/${id}`, data);
  };

  deleteProgram = async (id: string): Promise<void> => {
    await api.delete(`/api/programs/${id}`);
  };
}
