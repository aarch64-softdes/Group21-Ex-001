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

  const actions = React.useMemo(
    () => ({
      onSave: async (id: string, value: UpdateFacultyDTO) => {
        await updateFaculty.mutateAsync({
          id: parseInt(id, 10),
          data: value,
        });
      },
      onAdd: async (value: CreateFacultyDTO) => {
        await createFaculty.mutateAsync(value);
      },
      onDelete: async (id: string) => {
        await deleteFaculty.mutateAsync(parseInt(id, 10));
      },
    }),
    [updateFaculty, createFaculty, deleteFaculty],
  );

  return (
    <div className='min-h-3/4 w-full m-auto flex flex-row gap-4 p-4'>
      <GenericTable
        tableTitle='Faculty Management'
        addingTitle='Add Faculty'
        queryHook={useFaculties}
        columns={columns}
        actions={actions}
        formComponent={FacultyForm}
        detailComponent={FacultyDetail}
        disabledActions={{
          edit: false,
          delete: false,
        }}
        requireDeleteConfirmation={true}
        filterOptions={[]}
        disablePagination={true}
      />
    </div>
  );
};

export default FacultyPage;
