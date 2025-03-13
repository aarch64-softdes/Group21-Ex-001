import { ApiResponse } from "@/types/apiResponse";
import Student, {
  CreateStudentDTO,
  UpdateStudentDTO,
  mapToStudent,
} from "@/types/student";
import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080",
});

export default class StudentService {
  getStudents = async ({
    page = 1,
    size = 10,
    sortName = "studentId",
    sortType = "asc",
    search = "",
  }: {
    page?: number;
    size?: number;
    sortName?: string;
    sortType?: string;
    search?: string;
  }): Promise<ApiResponse<Student>> => {
    const response = await api.get("/api/students", {
      params: {
        page,
        sortName,
        sortType,
        search,
      },
    });

    // Assuming the API returns a list of students
    return {
      data: response.data.map(mapToStudent),
      totalItems: parseInt(
        response.headers["x-total-count"] || response.data.length
      ),
      totalPages: parseInt(
        response.headers["x-total-pages"] ||
          Math.ceil(response.data.length / size)
      ),
      currentPage: page,
    };
  };

  getStudent = async (id: string): Promise<Student> => {
    if (id == "") {
      return {
        id: "",
        studentId: "",
        name: "",
        email: "",
        phone: "",
        address: "",
        dob: new Date(),
        gender: "",
        faculty: "",
        status: "",
        program: "",
        course: 0,
      };
    }

    const response = await api.get(`/api/students/${id}`);
    return mapToStudent(response.data);
  };

  addNewStudent = async (data: CreateStudentDTO): Promise<void> => {
    await api.post("/api/students/", data);
  };

  updateStudent = async (id: string, data: UpdateStudentDTO): Promise<void> => {
    await api.patch(`/api/students/${id}`, data);
  };

  deleteStudent = async (id: string): Promise<void> => {
    await api.delete(`/api/students/${id}`);
  };
}
