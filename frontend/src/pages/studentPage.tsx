import React from "react";
import GenericTable from "@/components/table/GenericTable";
import StudentForm, {
  StudentFormSchema,
} from "@/components/student/StudentForm";

import {
  useStudents,
  useCreateStudent,
  useUpdateStudent,
  useDeleteStudent,
} from "@/hooks/useStudentApi";

import { SearchFilterOption } from "@/types/filter";
import Student, { CreateStudentDTO, UpdateStudentDTO } from "@/types/student";
import { Column } from "@/types/table";
import { Search } from "lucide-react";
import StudentDetail from "@/components/student/StudentDetail";

const StudentPage: React.FC = () => {
  const createStudent = useCreateStudent();
  const updateStudent = useUpdateStudent();
  const deleteStudent = useDeleteStudent();

  const columns: Column<Student>[] = React.useMemo(
    () => [
      {
        header: "Student ID",
        key: "studentId",
        editable: false,
        sortable: true,
        style: {
          width: "120px",
        },
      },
      {
        header: "Name",
        key: "name",
        editable: true,
        isDefaultSort: true,
        sortable: true,
        style: {
          maxWidth: "200px",
        },
        validate: (value: string) => {
          const result = StudentFormSchema.shape.name.safeParse(value);
          return result.success ? null : result.error.errors[0].message;
        },
      },
      {
        header: "Date of Birth",
        key: "dob",
        sortable: true,
        editable: true,
        style: {
          width: "150px",
        },
        validate: (value: string) => {
          const result = StudentFormSchema.shape.dob.safeParse(value);
          return result.success ? null : result.error.errors[0].message;
        },
        transform: (value: string) => {
          return new Date(value).toLocaleDateString();
        },
      },
      {
        header: "Gender",
        key: "gender",
        sortable: true,
        editable: true,
        style: {
          width: "100px",
        },
      },
      {
        header: "Faculty",
        key: "faculty",
        sortable: true,
        editable: true,
        style: {
          width: "150px",
        },
      },
      {
        header: "Course",
        key: "course",
        sortable: true,
        editable: true,
        style: {
          width: "80px",
        },
        validate: (value: number) => {
          const result = StudentFormSchema.shape.course.safeParse(
            parseInt(String(value), 10)
          );
          return result.success ? null : result.error.errors[0].message;
        },
      },
      {
        header: "Program",
        key: "program",
        sortable: true,
        editable: true,
        style: {
          width: "150px",
        },
      },
      {
        header: "Email",
        key: "email",
        sortable: true,
        editable: true,
        style: {
          width: "200px",
        },
        validate: (value: string) => {
          const result = StudentFormSchema.shape.email.safeParse(value);
          return result.success ? null : result.error.errors[0].message;
        },
      },
      {
        header: "Phone",
        key: "phone",
        sortable: true,
        editable: true,
        style: {
          width: "120px",
        },
        validate: (value: string) => {
          const result = StudentFormSchema.shape.phone.safeParse(value);
          return result.success ? null : result.error.errors[0].message;
        },
      },
      {
        header: "Status",
        key: "status",
        sortable: true,
        editable: true,
        style: {
          width: "112px",
        },

        // render: (value: string) => {
        //   const getStatusStyles = (status: string): string => {
        //     switch (status.toLowerCase()) {
        //       case "studying":
        //         return "bg-green-100 text-green-800 hover:bg-green-100";
        //       case "graduated":
        //         return "bg-blue-100 text-blue-800 hover:bg-blue-100";
        //       case "suspended":
        //         return "bg-amber-100 text-amber-800 hover:bg-amber-100";
        //       case "expelled":
        //         return "bg-red-100 text-red-800 hover:bg-red-100";
        //       case "on leave":
        //         return "bg-purple-100 text-purple-800 hover:bg-purple-100";
        //       default:
        //         return "bg-gray-100 text-gray-800 hover:bg-gray-100";
        //     }
        //   };

        //   return (
        //     <Badge
        //       className={`font-medium ${getStatusStyles(value)}`}
        //       variant="outline"
        //     >
        //       {value}
        //     </Badge>
        //   );
        // },
      },
    ],
    []
  );

  const actions = React.useMemo(
    () => ({
      onSave: async (id: string, value: UpdateStudentDTO) => {
        await updateStudent.mutateAsync({ id, data: value });
      },
      onAdd: async (value: CreateStudentDTO) => {
        await createStudent.mutateAsync(value);
      },
      onDelete: async (id: string) => {
        await deleteStudent.mutateAsync(id);
      },
    }),
    [updateStudent, createStudent, deleteStudent]
  );

  const searchFilterOption: SearchFilterOption = {
    id: "search",
    label: "Search",
    labelIcon: Search,
    placeholder: "Search by id, name",
    type: "search",
  };

  return (
    <div className="min-h-3/4 w-full m-auto flex flex-row gap-4 p-4">
      <GenericTable
        tableTitle="Student Management"
        addingTitle="Add Student"
        queryHook={useStudents}
        columns={columns}
        actions={actions}
        formComponent={StudentForm}
        detailComponent={StudentDetail}
        disabledActions={{
          edit: false,
          delete: false,
        }}
        requireDeleteConfirmation={true}
        filterOptions={[searchFilterOption]}
      />
    </div>
  );
};

export default StudentPage;
