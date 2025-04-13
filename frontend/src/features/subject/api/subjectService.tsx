import api from '@/core/config/api';
import { ApiResponse } from '@/core/types/apiResponse';
import Subject, {
  CreateSubjectDTO,
  UpdateSubjectDTO,
  mapToSubject,
} from '@/features/subject/types/subject';

export default class SubjectService {
  getSubjects = async ({
    page = 1,
    size = 10,
    sortBy = 'name',
    sortDirection = 'asc',
    search = '',
    faculty = '',
  }: {
    page?: number;
    size?: number;
    sortBy?: string;
    sortDirection?: string;
    search?: string;
    faculty?: string;
  }): Promise<ApiResponse<Subject>> => {
    const response = await api.get('/api/subjects', {
      params: {
        page,
        size,
        sortBy,
        sortDirection,
        search,
        faculty,
      },
    });

    return {
      data: response.data.content.data.map(mapToSubject),
      totalItems: response.data.content.totalElements,
      totalPages: response.data.content.totalPages,
      currentPage: response.data.content.pageNumber,
    };
  };

  getSubject = async (id: string): Promise<Subject> => {
    const response = await api.get(`/api/subjects/${id}`);

    return mapToSubject(response.data.content);
  };

  addNewSubject = async (data: CreateSubjectDTO): Promise<void> => {
    await api.post('/api/subjects', data);
  };

  updateSubject = async (id: string, data: UpdateSubjectDTO): Promise<void> => {
    await api.put(`/api/subjects/${id}`, data);
  };

  deleteSubject = async (id: string): Promise<void> => {
    await api.delete(`/api/subjects/${id}`);
  };
}
