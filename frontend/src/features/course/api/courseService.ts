import api from '@/core/config/api';
import { ApiResponse } from '@/core/types/apiResponse';
import Course, {
  CreateCourseDTO,
  UpdateCourseDTO,
  mapToCourse,
} from '../types/course';
import { formatSchedule } from '../types/courseSchedule';

export default class CourseService {
  getCourses = async ({
    page = 1,
    size = 10,
    sortName = 'id',
    sortType = 'asc',
  }: {
    page?: number;
    size?: number;
    sortName?: string;
    sortType?: string;
  }): Promise<ApiResponse<Course>> => {
    try {
      const response = await api.get('/api/courses', {
        params: {
          page,
          size,
          sortBy: sortName,
          sortDirection: sortType,
        },
      });

      // Handle the response based on the API structure
      return {
        data: response.data.content.data.map(mapToCourse),
        totalItems: response.data.content.totalElements,
        totalPages: response.data.content.totalPages,
        currentPage: response.data.content.pageNumber,
      };
    } catch (error) {
      console.error('Error fetching courses:', error);
      throw error;
    }
  };

  getCourse = async (id: string): Promise<Course> => {
    try {
      const response = await api.get(`/api/courses/${id}`);

      return mapToCourse(response.data.content);
    } catch (error) {
      console.error('Error fetching course:', error);
      throw error;
    }
  };

  addCourse = async (data: CreateCourseDTO): Promise<void> => {
    try {
      await api.post('/api/courses', data);
    } catch (error) {
      console.error('Error adding course:', error);
      throw error;
    }
  };

  updateCourse = async (id: string, data: UpdateCourseDTO): Promise<void> => {
    try {
      await api.put(`/api/courses/${id}`, data);
    } catch (error) {
      console.error('Error updating course:', error);
      throw error;
    }
  };

  deleteCourse = async (id: string): Promise<void> => {
    try {
      await api.delete(`/api/courses/${id}`);
    } catch (error) {
      console.error('Error deleting course:', error);
      throw error;
    }
  };
}
