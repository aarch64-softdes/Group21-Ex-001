import { ApiResponse } from '@/types/apiResponse';
import Student, {
  CreateStudentDTO,
  UpdateStudentDTO,
  mapToStudent,
} from '@/types/student';
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080',
});

export default class StudentService {
  getStudents = async ({
    page = 1,
    size = 10,
    sortName = 'studentId',
    sortType = 'asc',
    search = '',
  }: {
    page?: number;
    size?: number;
    sortName?: string;
    sortType?: string;
    search?: string;
  }): Promise<ApiResponse<Student>> => {
    const response = await api.get('/api/students', {
      params: {
        page,
        sortName,
        sortType,
        search,
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
        phone: '',
        dob: new Date(),
        gender: '',
        faculty: '',
        status: '',
        program: '',
        course: 0,
        citizenship: '',
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
        idDocument: {
          type: 'Identity Card',
          number: '',
          issuedDate: new Date(),
          expiryDate: new Date(),
          issuedBy: '',
        },
      };
    }

    const response = await api.get(`/api/students/${id}`);
    return mapToStudent(response.data.content); /// TODO: reformat the response
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
}
