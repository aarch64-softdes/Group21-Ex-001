import { ApiResponse } from '@/core/types/apiResponse';
import Class, {
  CreateClassDto,
  UpdateClassDto,
  mapToClass,
} from '@/features/class/types/class';

// Mock data
const mockClasses: Class[] = [
  {
    id: '1',
    subjectId: '1', // Introduction to Computer Science
    programId: '1', // Computer Science
    code: 'CS101-01',
    year: 2025,
    startAt: new Date('2025-01-15'),
    lecturer: 'Dr. John Smith',
    maxStudent: 40,
    schedule: 'T2(3-6)', // Monday 3rd-6th period
    room: 'A101',
  },
  {
    id: '2',
    subjectId: '1', // Introduction to Computer Science
    programId: '2', // Information Technology
    code: 'CS101-02',
    year: 2025,
    startAt: new Date('2025-01-15'),
    lecturer: 'Dr. Sarah Johnson',
    maxStudent: 35,
    schedule: 'T4(1-4)', // Wednesday 1st-4th period
    room: 'B202',
  },
  {
    id: '3',
    subjectId: '2', // Advanced Mathematics
    programId: '4', // Mathematics
    code: 'MATH301-01',
    year: 2025,
    startAt: new Date('2025-01-16'),
    lecturer: 'Prof. David Williams',
    maxStudent: 30,
    schedule: 'T3(6-9)', // Tuesday 6th-9th period
    room: 'C303',
  },
  {
    id: '4',
    subjectId: '3', // Data Structures and Algorithms
    programId: '3', // Software Engineering
    code: 'CS201-01',
    year: 2025,
    startAt: new Date('2025-01-17'),
    lecturer: 'Dr. Emily Chen',
    maxStudent: 45,
    schedule: 'T5(4-7)', // Thursday 4th-7th period
    room: 'A105',
  },
  {
    id: '5',
    subjectId: '4', // Introduction to Physics
    programId: '6', // Engineering
    code: 'PHYS101-01',
    year: 2025,
    startAt: new Date('2025-01-18'),
    lecturer: 'Prof. Michael Brown',
    maxStudent: 50,
    schedule: 'T6(1-4)', // Friday 1st-4th period
    room: 'D404',
  },
];

// Map to store subject name by id for mock data
const subjectNameMap = {
  '1': 'Introduction to Computer Science',
  '2': 'Advanced Mathematics',
  '3': 'Data Structures and Algorithms',
  '4': 'Introduction to Physics',
  '5': 'Database Management Systems',
};

// Map to store program name by id for mock data
const programNameMap = {
  '1': 'Computer Science',
  '2': 'Information Technology',
  '3': 'Software Engineering',
  '4': 'Mathematics',
  '5': 'Physics',
  '6': 'Engineering',
};

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
  }): Promise<
    ApiResponse<Class & { subjectName?: string; programName?: string }>
  > => {
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

    // Filter by subject
    if (subject) {
      filteredData = filteredData.filter((cls) => {
        const subjectName =
          subjectNameMap[cls.subjectId as keyof typeof subjectNameMap];
        return subjectName?.toLowerCase().includes(subject.toLowerCase());
      });
    }

    // Filter by lecturer
    if (lecturer) {
      filteredData = filteredData.filter((cls) =>
        cls.lecturer.toLowerCase().includes(lecturer.toLowerCase()),
      );
    }

    // Filter by year
    if (year) {
      filteredData = filteredData.filter((cls) => cls.year.toString() === year);
    }

    // Filter by program
    if (program) {
      filteredData = filteredData.filter((cls) => {
        const programName =
          programNameMap[cls.programId as keyof typeof programNameMap];
        return programName?.toLowerCase().includes(program.toLowerCase());
      });
    }

    // Sort data
    filteredData.sort((a, b) => {
      const aValue = a[sortName as keyof Class];
      const bValue = b[sortName as keyof Class];

      if (typeof aValue === 'string' && typeof bValue === 'string') {
        return sortType === 'asc'
          ? aValue.localeCompare(bValue)
          : bValue.localeCompare(aValue);
      }

      if (typeof aValue === 'number' && typeof bValue === 'number') {
        return sortType === 'asc' ? aValue - bValue : bValue - aValue;
      }

      if (aValue instanceof Date && bValue instanceof Date) {
        return sortType === 'asc'
          ? aValue.getTime() - bValue.getTime()
          : bValue.getTime() - aValue.getTime();
      }

      return 0;
    });

    // Enhance data with subject and program names (for display in the table)
    const enhancedData = filteredData.map((cls) => ({
      ...cls,
      subjectName: subjectNameMap[cls.subjectId as keyof typeof subjectNameMap],
      programName: programNameMap[cls.programId as keyof typeof programNameMap],
    }));

    // Paginate data
    const start = (page - 1) * size;
    const end = start + size;
    const paginatedData = enhancedData.slice(start, end);

    return {
      data: paginatedData,
      totalItems: enhancedData.length,
      totalPages: Math.ceil(enhancedData.length / size),
      currentPage: page,
    };
  };

  getClass = async (
    id: string,
  ): Promise<Class & { subjectName?: string; programName?: string }> => {
    // Simulate API delay
    await new Promise((resolve) => setTimeout(resolve, 300));

    const classItem = mockClasses.find((c) => c.id === id);

    if (!classItem) {
      throw new Error('Class not found');
    }

    return {
      ...classItem,
      subjectName:
        subjectNameMap[classItem.subjectId as keyof typeof subjectNameMap],
      programName:
        programNameMap[classItem.programId as keyof typeof programNameMap],
    };
  };

  // Get all subjects for dropdown
  getSubjects = async (): Promise<Array<{ id: string; name: string }>> => {
    // Simulate API delay
    await new Promise((resolve) => setTimeout(resolve, 300));

    return Object.entries(subjectNameMap).map(([id, name]) => ({
      id,
      name,
    }));
  };

  // Get all programs for dropdown
  getPrograms = async (): Promise<Array<{ id: string; name: string }>> => {
    // Simulate API delay
    await new Promise((resolve) => setTimeout(resolve, 300));

    return Object.entries(programNameMap).map(([id, name]) => ({
      id,
      name,
    }));
  };

  addClass = async (data: CreateClassDto): Promise<void> => {
    // Simulate API delay
    await new Promise((resolve) => setTimeout(resolve, 500));

    const newId = (mockClasses.length + 1).toString();

    mockClasses.push({
      id: newId,
      ...data,
    });
  };

  updateClass = async (id: string, data: UpdateClassDto): Promise<void> => {
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
  };

  deleteClass = async (id: string): Promise<void> => {
    // Simulate API delay
    await new Promise((resolve) => setTimeout(resolve, 500));

    const index = mockClasses.findIndex((c) => c.id === id);

    if (index === -1) {
      throw new Error('Class not found');
    }

    mockClasses.splice(index, 1);
  };
}
