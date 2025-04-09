import { ApiResponse } from '@/core/types/apiResponse';
import Class, { CreateClassDto, UpdateClassDto } from '../types/class';

// Mock data for testing and development
const mockClasses: Class[] = [
  {
    id: '1',
    subjectId: '1',
    code: 'CS101-01',
    year: 2025,
    startAt: new Date('2025-01-15'),
    lecturer: 'Dr. John Smith',
    maxStudent: 40,
    schedule: 'T2(3-6)', // Monday 3rd-6th period
    room: 'A101',
    subject: {
      id: '1',
      name: 'Introduction to Computer Science',
      code: 'CS101',
      credits: 3,
      description: 'A basic introduction to computer science principles',
      faculty: 'Computer Science',
      prerequisites: [],
    },
    program: {
      id: '1',
      name: 'Computer Science',
    },
  },
  {
    id: '2',
    subjectId: '1',
    code: 'CS101-02',
    year: 2025,
    startAt: new Date('2025-01-15'),
    lecturer: 'Dr. Sarah Johnson',
    maxStudent: 35,
    schedule: 'T4(1-4)', // Wednesday 1st-4th period
    room: 'B202',
    subject: {
      id: '1',
      name: 'Introduction to Computer Science',
      code: 'CS101',
      credits: 3,
      description: 'A basic introduction to computer science principles',
      faculty: 'Computer Science',
      prerequisites: [],
    },
    program: {
      id: '2',
      name: 'Information Technology',
    },
  },
  {
    id: '3',
    subjectId: '2',
    code: 'MATH301-01',
    year: 2025,
    startAt: new Date('2025-01-16'),
    lecturer: 'Prof. David Williams',
    maxStudent: 30,
    schedule: 'T3(6-9)', // Tuesday 6th-9th period
    room: 'C303',
    subject: {
      id: '2',
      name: 'Advanced Mathematics',
      code: 'MATH301',
      credits: 4,
      description:
        'Advanced topics in mathematics including calculus and linear algebra',
      faculty: 'Mathematics',
      prerequisites: [],
    },
    program: {
      id: '4',
      name: 'Mathematics',
    },
  },
  {
    id: '4',
    subjectId: '3',
    code: 'CS201-01',
    year: 2025,
    startAt: new Date('2025-01-17'),
    lecturer: 'Dr. Emily Chen',
    maxStudent: 45,
    schedule: 'T5(4-7)', // Thursday 4th-7th period
    room: 'A105',
    subject: {
      id: '3',
      name: 'Data Structures and Algorithms',
      code: 'CS201',
      credits: 4,
      description: 'Study of data structures and fundamental algorithms',
      faculty: 'Computer Science',
      prerequisites: ['1'],
    },
    program: {
      id: '3',
      name: 'Software Engineering',
    },
  },
  {
    id: '5',
    subjectId: '4',
    code: 'PHYS101-01',
    year: 2025,
    startAt: new Date('2025-01-18'),
    lecturer: 'Prof. Michael Brown',
    maxStudent: 50,
    schedule: 'T6(1-4)', // Friday 1st-4th period
    room: 'D404',
    subject: {
      id: '4',
      name: 'Introduction to Physics',
      code: 'PHYS101',
      credits: 3,
      description: 'Basic principles of physics and mechanics',
      faculty: 'Physics',
      prerequisites: [],
    },
    program: {
      id: '6',
      name: 'Engineering',
    },
  },
];

export default class ClassService {
  getClasses = async ({
    page = 1,
    size = 10,
    sortName = 'code',
    sortType = 'asc',
    search = '',
    subject = '',
    lecturer = '',
    year = '',
    program = '',
  }: {
    page?: number;
    size?: number;
    sortName?: string;
    sortType?: string;
    search?: string;
    subject?: string;
    lecturer?: string;
    year?: string;
    program?: string;
  }): Promise<ApiResponse<Class>> => {
    try {
      // For real API implementation
      // const response = await api.get('/api/classes', {
      //   params: {
      //     page,
      //     size,
      //     sortName,
      //     sortType,
      //     search,
      //     subject,
      //     lecturer,
      //     year,
      //     program,
      //   },
      // });
      //
      // return {
      //   data: response.data.content.data.map(mapToClass),
      //   totalItems: response.data.content.page.totalItems,
      //   totalPages: response.data.content.page.totalPages,
      //   currentPage: response.data.content.page.pageNumber,
      // };

      // Simulate API delay
      await new Promise((resolve) => setTimeout(resolve, 500));

      // Clone the mock data to avoid modifying the original
      let filteredData = [...mockClasses];

      // Filter by search term (code or room)
      if (search) {
        const searchLower = search.toLowerCase();
        filteredData = filteredData.filter(
          (cls) =>
            cls.code.toLowerCase().includes(searchLower) ||
            cls.room.toLowerCase().includes(searchLower),
        );
      }

      // Filter by subject name
      if (subject) {
        filteredData = filteredData.filter((cls) =>
          cls.subject?.name.toLowerCase().includes(subject.toLowerCase()),
        );
      }

      // Filter by lecturer
      if (lecturer) {
        filteredData = filteredData.filter((cls) =>
          cls.lecturer.toLowerCase().includes(lecturer.toLowerCase()),
        );
      }

      // Filter by year
      if (year) {
        filteredData = filteredData.filter(
          (cls) => cls.year.toString() === year,
        );
      }

      // Filter by program name
      if (program) {
        filteredData = filteredData.filter((cls) =>
          cls.program?.name.toLowerCase().includes(program.toLowerCase()),
        );
      }

      // Paginate data
      const start = (page - 1) * size;
      const end = start + size;
      const paginatedData = filteredData.slice(start, end);

      return {
        data: paginatedData,
        totalItems: filteredData.length,
        totalPages: Math.ceil(filteredData.length / size),
        currentPage: page,
      };
    } catch (error) {
      console.error('Error fetching classes:', error);
      throw error;
    }
  };

  getClass = async (id: string): Promise<Class> => {
    try {
      // For real API implementation
      // const response = await api.get(`/api/classes/${id}`);
      // return mapToClass(response.data.content);

      // Simulate API delay
      await new Promise((resolve) => setTimeout(resolve, 300));

      const classItem = mockClasses.find((c) => c.id === id);

      if (!classItem) {
        throw new Error('Class not found');
      }

      return classItem;
    } catch (error) {
      console.error('Error fetching class:', error);
      throw error;
    }
  };

  addClass = async (data: CreateClassDto): Promise<void> => {
    try {
      // For real API implementation
      // await api.post('/api/classes', data);

      // Simulate API delay
      await new Promise((resolve) => setTimeout(resolve, 500));

      const newId = (mockClasses.length + 1).toString();

      // Create a new class with the provided data
      const newClass: Class = {
        id: newId,
        subjectId: data.subjectId,
        code: data.code,
        year: data.year,
        startAt: data.startAt,
        lecturer: data.lecturer,
        maxStudent: data.maxStudent,
        schedule: data.schedule,
        room: data.room,
      };

      mockClasses.push(newClass);
    } catch (error) {
      console.error('Error adding class:', error);
      throw error;
    }
  };

  updateClass = async (id: string, data: UpdateClassDto): Promise<void> => {
    try {
      // For real API implementation
      // await api.put(`/api/classes/${id}`, data);

      // Simulate API delay
      await new Promise((resolve) => setTimeout(resolve, 500));

      const index = mockClasses.findIndex((c) => c.id === id);

      if (index === -1) {
        throw new Error('Class not found');
      }

      mockClasses[index] = {
        ...mockClasses[index],
        ...data,
      };
    } catch (error) {
      console.error('Error updating class:', error);
      throw error;
    }
  };

  deleteClass = async (id: string): Promise<void> => {
    try {
      // For real API implementation
      // await api.delete(`/api/classes/${id}`);

      // Simulate API delay
      await new Promise((resolve) => setTimeout(resolve, 500));

      const index = mockClasses.findIndex((c) => c.id === id);

      if (index === -1) {
        throw new Error('Class not found');
      }

      mockClasses.splice(index, 1);
    } catch (error) {
      console.error('Error deleting class:', error);
      throw error;
    }
  };
}
