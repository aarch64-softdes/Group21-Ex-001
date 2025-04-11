import GenericTable from '@components/table/GenericTable';
import {
  useCreateSubject,
  useDeleteSubject,
  useSubjects,
  useUpdateSubject,
} from '@/features/subject/api/useSubjectApi';
import Subject, {
  CreateSubjectDTO,
  UpdateSubjectDTO,
} from '@/features/subject/types/subject';
import { Column } from '@/core/types/table';
import React from 'react';
import { SearchFilterOption } from '@/core/types/filter';
import { BookOpen, FileSearch } from 'lucide-react';
import SubjectForm from '@subject/components/SubjectForm';
import SubjectDetail from '@subject/components/SubjectDetail';

const SubjectPage: React.FC = () => {
  const createSubject = useCreateSubject();
  const updateSubject = useUpdateSubject();
  const deleteSubject = useDeleteSubject();

  const columns: Column<Subject>[] = React.useMemo(
    () => [
      {
        header: 'ID',
        key: 'id',
        style: {
          width: '80px',
        },
      },
      {
        header: 'Code',
        key: 'code',
        style: {
          width: '120px',
        },
      },
      {
        header: 'Name',
        key: 'name',
        isDefaultSort: true,
      },
      {
        header: 'Credits',
        key: 'credits',
        style: {
          width: '100px',
        },
      },
      {
        header: 'Faculty',
        key: 'faculty',
      },
    ],
    [],
  );

  const actions = React.useMemo(
    () => ({
      onSave: async (id: string, value: UpdateSubjectDTO) => {
        await updateSubject.mutateAsync({
          id,
          data: value,
        });
      },
      onAdd: async (value: CreateSubjectDTO) => {
        await createSubject.mutateAsync(value);
      },
      onDelete: async (id: string) => {
        await deleteSubject.mutateAsync(id);
      },
    }),
    [updateSubject, createSubject, deleteSubject],
  );

  const searchNameFilterOption: SearchFilterOption = {
    id: 'search',
    label: 'Search by name or code',
    labelIcon: BookOpen,
    placeholder: 'Search by name or code',
    type: 'search',
  };

  const searchFacultyFilterOption: SearchFilterOption = {
    id: 'faculty',
    label: 'Search by faculty',
    labelIcon: FileSearch,
    placeholder: 'Search by faculty',
    type: 'search',
  };

  return (
    <div className='min-h-3/4 w-full m-auto flex flex-row gap-4 p-4'>
      <GenericTable
        tableTitle='Subject Management'
        addingTitle='Add Subject'
        queryHook={useSubjects}
        columns={columns}
        actions={actions}
        formComponent={SubjectForm}
        detailComponent={SubjectDetail}
        disabledActions={{
          edit: false,
          delete: false,
        }}
        requireDeleteConfirmation={true}
        filterOptions={[searchNameFilterOption, searchFacultyFilterOption]}
      />
    </div>
  );
};

export default SubjectPage;
