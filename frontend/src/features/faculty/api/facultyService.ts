import api from '@/core/config/api';
import { ApiResponse } from '@/core/types/apiResponse';
import Faculty, {
  CreateFacultyDTO,
  UpdateFacultyDTO,
  mapToFaculty,
} from '@faculty/types/faculty';

export default class FacultyService {
  getFaculties = async ({
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
  }): Promise<ApiResponse<Faculty>> => {
    const response = await api.get('/api/faculties', {
      params: {
        page,
        size,
        sortName,
        sortType,
        search,
      },
    });

    return {
      data: response.data.content.data.map(mapToFaculty),
      totalItems: response.data.content.page.totalElements,
      totalPages: response.data.content.page.totalPages,
      currentPage: response.data.content.page.pageNumber,
    };
  };

  getFaculty = async (id: string): Promise<Faculty> => {
    const response = await api.get(`/api/faculties/${id}`);
    return mapToFaculty(response.data.content);
  };

  addNewFaculty = async (data: CreateFacultyDTO): Promise<void> => {
    await api.post('/api/faculties', data);
  };

  updateFaculty = async (id: string, data: UpdateFacultyDTO): Promise<void> => {
    await api.put(`/api/faculties/${id}`, data);
  };

  deleteFaculty = async (id: string): Promise<void> => {
    await api.delete(`/api/faculties/${id}`);
  };
}
