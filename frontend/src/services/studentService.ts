import api from '@/lib/api';
import { ApiResponse } from '@/types/apiResponse';
import Student, {
  CreateStudentDTO,
  UpdateStudentDTO,
  mapToStudent,
} from '@/types/student';

export default class StudentService {
  getStudents = async ({
    page = 1,
    size = 10,
    sortName = 'studentId',
    sortType = 'asc',
    search = '',
    faculty = '',
  }: {
    page?: number;
    size?: number;
    sortName?: string;
    sortType?: string;
    search?: string;
    faculty?: string;
  }): Promise<ApiResponse<Student>> => {
    const response = await api.get('/api/students', {
      params: {
        page,
        size,
        sortName,
        sortType,
        search,
        faculty,
      },
    });

    // Assuming the API returns a list of students
    return {
      data: response.data.content.data.map(mapToStudent),
      totalItems: response.data.content.page.totalElements,
      totalPages: response.data.content.page.totalPages,
      currentPage: page,
    };
  };

  getStudent = async (id: string): Promise<Student> => {
    if (id == '') {
      return {
        id: '',
        studentId: '',
        name: '',
        email: '',
        phone: {
          countryCode: '',
          phoneNumber: '',
        },
        dob: new Date(),
        gender: '',
        faculty: '',
        status: '',
        program: '',
        course: 0,
        permanentAddress: {
          id: '',
          street: '',
          ward: '',
          district: '',
          province: '',
          country: '',
        },
        temporaryAddress: {
          id: '',
          street: '',
          ward: '',
          district: '',
          province: '',
          country: '',
        },
        mailingAddress: {
          id: '',
          street: '',
          ward: '',
          district: '',
          province: '',
          country: '',
        },
        identity: {
          type: 'Identity Card',
          number: '',
          issuedDate: new Date(),
          expiryDate: new Date(),
          issuedBy: '',
        },
      };
    }

    const response = await api.get(`/api/students/${id}`);
    return mapToStudent(response.data.content);
  };

  addNewStudent = async (data: CreateStudentDTO): Promise<void> => {
    await api.post('/api/students/', data);
  };

  updateStudent = async (id: string, data: UpdateStudentDTO): Promise<void> => {
    await api.patch(`/api/students/${id}`, data);
  };

  deleteStudent = async (id: string): Promise<void> => {
    await api.delete(`/api/students/${id}`);
  };

  exportStudents = async (format: string): Promise<Blob> => {
    const response = await api.get('/api/files/export', {
      params: { format },
      responseType: 'blob',
    });
    return response.data;
  };

  importStudents = async (format: string, file: File): Promise<void> => {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('format', format);

    await api.post('/api/files/import', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
  };
}
