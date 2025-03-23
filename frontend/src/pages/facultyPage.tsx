import FacultyDetail from '@/components/faculty/FacultyDetail';
import FacultyForm from '@/components/faculty/FacultyForm';
import GenericTable from '@/components/table/GenericTable';
import {
  useCreateFaculty,
  useDeleteFaculty,
  useFaculties,
  useUpdateFaculty,
} from '@/hooks/api/useFacultyApi';
import Faculty, { CreateFacultyDTO, UpdateFacultyDTO } from '@/types/faculty';
import { Column } from '@/types/table';
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
