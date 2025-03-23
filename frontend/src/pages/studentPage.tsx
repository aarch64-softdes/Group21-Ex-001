import StudentForm from '@/components/student/StudentForm';
import GenericTable from '@/components/table/GenericTable';
import React, { useCallback } from 'react';

import {
  useCreateStudent,
  useDeleteStudent,
  useStudents,
  useUpdateStudent,
} from '@/hooks/api/useStudentApi';

import StudentDetail from '@/components/student/StudentDetail';
import { SearchFilterOption } from '@/types/filter';
import Student, { CreateStudentDTO } from '@/types/student';
import { Column } from '@/types/table';
import { FolderSearch, UserSearch } from 'lucide-react';
import StudentService from '@/services/studentService';

const StudentPage: React.FC = () => {
  const studentService = new StudentService();

  const createStudent = useCreateStudent();
  const updateStudent = useUpdateStudent();
  const deleteStudent = useDeleteStudent();

  const columns: Column<Student>[] = React.useMemo(
    () => [
      {
        header: 'Student ID',
        key: 'studentId',
        style: {
          width: '120px',
        },
      },
      {
        header: 'Name',
        key: 'name',
        isDefaultSort: true,
        sortable: false,
        style: {
          maxWidth: '200px',
        },
      },
      {
        header: 'Date of Birth',
        key: 'dob',
        style: {
          width: '150px',
        },
        transform: (value: string) => {
          return new Date(value).toLocaleDateString();
        },
      },
      {
        header: 'Gender',
        key: 'gender',
        style: {
          width: '100px',
        },
      },
      {
        header: 'Faculty',
        key: 'faculty',
        style: {
          width: '150px',
        },
      },
      {
        header: 'Course',
        key: 'course',
        style: {
          width: '80px',
        },
      },
      {
        header: 'Program',
        key: 'program',
        style: {
          width: '150px',
        },
      },
      {
        header: 'Status',
        key: 'status',
        style: {
          width: '112px',
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
    [],
  );

  const actions = React.useMemo(
    () => ({
      onSave: async (id: string, value: CreateStudentDTO) => {
        await updateStudent.mutateAsync({ id, data: value });
      },
      onAdd: async (value: CreateStudentDTO) => {
        await createStudent.mutateAsync(value);
      },
      onDelete: async (id: string) => {
        await deleteStudent.mutateAsync(id);
      },
    }),
    [updateStudent, createStudent, deleteStudent],
  );

  const searchNameFilterOption: SearchFilterOption = {
    id: 'search',
    label: 'Search by name',
    labelIcon: UserSearch,
    placeholder: 'Search by id, name',
    type: 'search',
  };

  const searchFacultyFilterOption: SearchFilterOption = {
    id: 'faculty',
    label: 'Search by faculty',
    labelIcon: FolderSearch,
    placeholder: 'Search by faculty',
    type: 'search',
  };

  const handleExportStudents = useCallback(async (format: string) => {
    return studentService.exportStudents(format);
  }, []);

  const handleImportStudents = useCallback(
    async (format: string, file: File) => {
      return studentService.importStudents(format, file);
    },
    [],
  );

  return (
    <div className='min-h-3/4 m-auto flex flex-row gap-4 p-4'>
      <GenericTable
        tableTitle='Student Management'
        addingTitle='Add Student'
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
        filterOptions={[searchNameFilterOption, searchFacultyFilterOption]}
        fileOptions={{
          enableExport: true,
          onExport: handleExportStudents,
          enableImport: true,
          onImport: handleImportStudents,
        }}
      />
    </div>
  );
};

export default StudentPage;
