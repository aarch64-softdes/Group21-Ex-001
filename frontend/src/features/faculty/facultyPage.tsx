import FacultyDetail from '@/features/faculty/components/FacultyDetail';
import FacultyForm from '@/features/faculty/components/FacultyForm';
import GenericTable from '@components/table/GenericTable';
import {
  useCreateFaculty,
  useDeleteFaculty,
  useFaculties,
  useUpdateFaculty,
} from '@/features/faculty/api/useFacultyApi';
import Faculty, {
  CreateFacultyDTO,
  UpdateFacultyDTO,
} from '@/features/faculty/types/faculty';
import { Column } from '@/core/types/table';
import React from 'react';

const FacultyPage: React.FC = () => {
  const createFaculty = useCreateFaculty();
  const updateFaculty = useUpdateFaculty();
  const deleteFaculty = useDeleteFaculty();

  const columns: Column<Faculty>[] = React.useMemo(
    () => [
      {
        header: 'ID',
        key: 'id',
        style: {
          width: '80px',
        },
      },
      {
        header: 'Name',
        key: 'name',
      },
    ],
    [],
  );

  const onSave = React.useCallback(
    async (id: string, value: UpdateFacultyDTO) => {
      await updateFaculty.mutateAsync({
        id: id,
        data: value,
      });
    },
    [updateFaculty],
  );

  const onDelete = React.useCallback(
    async (id: string) => {
      await deleteFaculty.mutateAsync(id);
    },
    [deleteFaculty],
  );

  const onAdd = React.useCallback(
    async (value: CreateFacultyDTO) => {
      await createFaculty.mutateAsync(value);
    },
    [createFaculty],
  );

  return (
    <div className='min-h-3/4 w-full m-auto flex flex-row gap-4 p-4'>
      <GenericTable
        tableTitle='Faculty Management'
        addAction={{
          onAdd,
          title: 'Add Faculty',
        }}
        queryHook={useFaculties}
        columns={columns}
        actionCellProperties={{
          requireDeleteConfirmation: true,
          edit: {
            onSave: onSave,
          },
          delete: {
            onDelete: onDelete,
          },
          detailComponent: FacultyDetail,
          formComponent: FacultyForm,
        }}
        filterOptions={[]}
      />
    </div>
  );
};

export default FacultyPage;
