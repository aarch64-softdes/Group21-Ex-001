import ProgramDetail from '@/features/program/components/ProgramDetail';
import ProgramForm from '@/features/program/components/ProgramForm';
import GenericTable from '@components/table/GenericTable';
import {
  useCreateProgram,
  useDeleteProgram,
  usePrograms,
  useUpdateProgram,
} from '@/features/program/api/useProgramApi';
import Program, {
  CreateProgramDTO,
  UpdateProgramDTO,
} from '@/features/program/types/program';
import { Column } from '@/core/types/table';
import React from 'react';

const ProgramPage: React.FC = () => {
  const createProgram = useCreateProgram();
  const updateProgram = useUpdateProgram();
  const deleteProgram = useDeleteProgram();

  const columns: Column<Program>[] = React.useMemo(
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
      onSave: async (id: string, value: UpdateProgramDTO) => {
        await updateProgram.mutateAsync({
          id: id,
          data: value,
        });
      },
      onAdd: async (value: CreateProgramDTO) => {
        await createProgram.mutateAsync(value);
      },
      onDelete: async (id: string) => {
        await deleteProgram.mutateAsync(id);
      },
    }),
    [updateProgram, createProgram, deleteProgram],
  );

  return (
    <div className='min-h-3/4 w-full m-auto flex flex-row gap-4 p-4'>
      <GenericTable
        tableTitle='Program Management'
        addingTitle='Add Program'
        queryHook={usePrograms}
        columns={columns}
        actions={actions}
        formComponent={ProgramForm}
        detailComponent={ProgramDetail}
        disabledActions={{
          edit: false,
          delete: false,
        }}
        requireDeleteConfirmation={true}
        filterOptions={[]}
      />
    </div>
  );
};

export default ProgramPage;
