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
    sortName = 'name',
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
  }): Promise<ApiResponse<Subject>> => {
    // Simulate API delay
    await new Promise((resolve) => setTimeout(resolve, 500));

    // Filter data based on search term
    let filteredData = [...mockSubjects];

    if (search) {
      const searchLower = search.toLowerCase();
      filteredData = filteredData.filter(
        (subject) =>
          subject.name.toLowerCase().includes(searchLower) ||
          subject.code.toLowerCase().includes(searchLower),
      );
    }

    if (faculty) {
      filteredData = filteredData.filter((subject) =>
        subject.faculty?.toLowerCase().includes(faculty.toLowerCase()),
      );
    }

    // Sort data
    filteredData.sort((a, b) => {
      const aValue = a[sortName as keyof Subject];
      const bValue = b[sortName as keyof Subject];

      if (typeof aValue === 'string' && typeof bValue === 'string') {
        return sortType === 'asc'
          ? aValue.localeCompare(bValue)
          : bValue.localeCompare(aValue);
      }

      if (typeof aValue === 'number' && typeof bValue === 'number') {
        return sortType === 'asc' ? aValue - bValue : bValue - aValue;
      }

      return 0;
    });

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
  };

  getSubject = async (
    id: string,
  ): Promise<Subject & { prerequisiteNames?: string[] }> => {
    // Simulate API delay
    await new Promise((resolve) => setTimeout(resolve, 300));

    const subject = mockSubjects.find((s) => s.id === id);

    if (!subject) {
      throw new Error('Subject not found');
    }

    // Get names of prerequisites
    const prerequisiteNames =
      subject.prerequisites
        ?.map((prereqId) => {
          const prereq = mockSubjects.find((s) => s.id === prereqId);
          return prereq ? `${prereq.code} - ${prereq.name}` : '';
        })
        .filter((name) => name !== '') || [];

    return {
      ...subject,
      prerequisiteNames,
    };
  };

  addNewSubject = async (data: CreateSubjectDTO): Promise<void> => {
    // Simulate API delay
    await new Promise((resolve) => setTimeout(resolve, 500));

    const newId = (mockSubjects.length + 1).toString();

    mockSubjects.push({
      id: newId,
      ...data,
    });
  };

  updateSubject = async (id: string, data: UpdateSubjectDTO): Promise<void> => {
    // Simulate API delay
    await new Promise((resolve) => setTimeout(resolve, 500));

    const index = mockSubjects.findIndex((s) => s.id === id);

    if (index === -1) {
      throw new Error('Subject not found');
    }

    mockSubjects[index] = {
      ...mockSubjects[index],
      ...data,
    };
  };

  deleteSubject = async (id: string): Promise<void> => {
    // Simulate API delay
    await new Promise((resolve) => setTimeout(resolve, 500));

    const index = mockSubjects.findIndex((s) => s.id === id);

    if (index === -1) {
      throw new Error('Subject not found');
    }

    mockSubjects.splice(index, 1);
  };
}

// Mock data
const mockSubjects: Subject[] = [
  {
    id: '1',
    name: 'Introduction to Computer Science',
    code: 'CS101',
    credits: 3,
    description: 'A basic introduction to computer science principles',
    faculty: 'Computer Science',
    prerequisites: [],
  },
  {
    id: '2',
    name: 'Advanced Mathematics',
    code: 'MATH301',
    credits: 4,
    description:
      'Advanced topics in mathematics including calculus and linear algebra',
    faculty: 'Mathematics',
    prerequisites: [],
  },
  {
    id: '3',
    name: 'Data Structures and Algorithms',
    code: 'CS201',
    credits: 4,
    description: 'Study of data structures and fundamental algorithms',
    faculty: 'Computer Science',
    prerequisites: ['1'], // Requires Introduction to Computer Science
  },
  {
    id: '4',
    name: 'Introduction to Physics',
    code: 'PHYS101',
    credits: 3,
    description: 'Basic principles of physics and mechanics',
    faculty: 'Physics',
    prerequisites: [],
  },
  {
    id: '5',
    name: 'Database Management Systems',
    code: 'CS301',
    credits: 3,
    description: 'Design and implementation of database systems',
    faculty: 'Computer Science',
    prerequisites: ['1', '3'], // Requires Intro to CS and Data Structures
  },
];
