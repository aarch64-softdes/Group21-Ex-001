import GenericTable from '@components/table/GenericTable';
import StudentForm from '@student/components/StudentForm';
import React, { useCallback } from 'react';

import {
  useCreateStudent,
  useDeleteStudent,
  useStudents,
  useUpdateStudent,
} from '@/features/student/api/useStudentApi';

import { SearchFilterOption } from '@/core/types/filter';
import { Column } from '@/core/types/table';
import StudentService from '@/features/student/api/studentService';
import Student, { CreateStudentDTO } from '@/features/student/types/student';
import StudentDetail from '@student/components/StudentDetail';
import { t } from 'i18next';
import { FolderSearch, UserSearch } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

const StudentPage: React.FC = () => {
  const studentService = new StudentService();
  const navigate = useNavigate();

  const createStudent = useCreateStudent();
  const updateStudent = useUpdateStudent();
  const deleteStudent = useDeleteStudent();

  const columns: Column<Student>[] = React.useMemo(
    () => [
      {
        header: t('student:fields.studentId'),
        key: 'studentId',
        style: {
          width: '120px',
        },
      },
      {
        header: t('student:fields.name'),
        key: 'name',
        isDefaultSort: true,
        sortable: false,
        style: {
          maxWidth: '200px',
        },
      },
      {
        header: t('student:fields.dob'),
        key: 'dob',
        style: {
          width: '150px',
        },
        transform: (value: string) => {
          return new Date(value).toLocaleDateString();
        },
      },
      {
        header: t('student:fields.gender'),
        key: 'gender',
        style: {
          width: '100px',
        },
      },
      {
        header: t('student:fields.faculty'),
        key: 'faculty',
        style: {
          width: '150px',
        },
      },

      {
        header: t('student:fields.schoolYear'),
        key: 'schoolYear',
        style: {
          width: '80px',
        },
      },
      {
        header: t('student:fields.program'),
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
        //       case "on leave":enrollment
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
    label: t('common:actions.search'),
    labelIcon: UserSearch,
    placeholder: t('student:search.nameIdPlaceholder'),
    type: 'search',
  };

  const searchFacultyFilterOption: SearchFilterOption = {
    id: 'faculty',
    label: t('student:search.byFaculty'),
    labelIcon: FolderSearch,
    placeholder: t('student:search.facultyPlaceholder'),
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
        tableTitle={t('student:title')}
        addingTitle={t('student:addNew')}
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
        additionalActions={[
          {
            label: t('student:enrollments'),
            handler(id) {
              navigate(`/student/${id}/enrollments`);
            },
          },
        ]}
      />
    </div>
  );
};

export default StudentPage;
