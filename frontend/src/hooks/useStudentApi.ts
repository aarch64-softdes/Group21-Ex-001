import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { QueryHookParams } from "@/types/table";
import StudentService from "@/services/studentService";
import { CreateStudentDTO, UpdateStudentDTO } from "@/types/student";

const studentService = new StudentService();

export const useStudents = (params: QueryHookParams) => {
  let { page, pageSize, filters, sort } = params;

  let search = "";
  if (filters.search && typeof filters.search === "string") {
    search = filters.search;
  }

  if (page < 1) {
    page = 1;
  }

  // Map sort config to API parameters
  const sortName = mapSortKeyToEntityProperty(sort.key);
  const sortType = sort.direction || "asc";

  return useQuery({
    queryKey: ["students", page, pageSize, search, sortName, sortType],
    queryFn: () =>
      studentService.getStudents({
        page,
        size: pageSize,
        sortName,
        sortType,
        search,
      }),
  });
};

export const useCreateStudent = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: CreateStudentDTO) => studentService.addNewStudent(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["students"] });
    },
  });
};

export const useUpdateStudent = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, data }: { id: string; data: UpdateStudentDTO }) =>
      studentService.updateStudent(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["students"] });
    },
  });
};

export const useDeleteStudent = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (id: string) => studentService.deleteStudent(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["students"] });
    },
  });
};

function mapSortKeyToEntityProperty(key: string | null): string {
  if (!key) return "studentId";

  const sortKeyMap: Record<string, string> = {
    studentId: "studentId",
    name: "name",
    dob: "dob",
    gender: "gender",
    faculty: "faculty",
    course: "course",
    program: "program",
    email: "email",
    address: "address",
    phone: "phone",
    status: "status",
  };

  return sortKeyMap[key] || key.toLowerCase();
}
