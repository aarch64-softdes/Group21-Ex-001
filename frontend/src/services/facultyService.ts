import api from '@/lib/api';
import { ApiResponse } from '@/types/apiResponse';
import Faculty, {
  CreateFacultyDTO,
  UpdateFacultyDTO,
  mapToFaculty,
} from '@/types/faculty';

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
      totalItems: response.data.content.data.length,
      totalPages: 1, // Since pagination is not implemented in these entities
      currentPage: page,
    };
  };

  getFaculty = async (id: number): Promise<Faculty> => {
    const response = await api.get(`/api/faculties/${id}`);
    return mapToFaculty(response.data.content);
  };

  addNewFaculty = async (data: CreateFacultyDTO): Promise<void> => {
    await api.post('/api/faculties', data);
  };

  updateFaculty = async (id: number, data: UpdateFacultyDTO): Promise<void> => {
    await api.put(`/api/faculties/${id}`, data);
  };

  deleteFaculty = async (id: number): Promise<void> => {
    await api.delete(`/api/faculties/${id}`);
  };
}
