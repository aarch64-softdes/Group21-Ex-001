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
import { useTranslation } from 'react-i18next';

const ProgramPage: React.FC = () => {
  const { t } = useTranslation(['program', 'common']);

  const createProgram = useCreateProgram();
  const updateProgram = useUpdateProgram();
  const deleteProgram = useDeleteProgram();

  const columns: Column<Program>[] = React.useMemo(
    () => [
      {
        header: t('program:fields.id'),
        key: 'id',
        style: {
          width: '80px',
        },
      },
      {
        header: t('program:fields.name'),
        key: 'name',
      },
    ],
    [t],
  );

  const onSave = React.useCallback(
    async (id: string, value: UpdateProgramDTO) => {
      await updateProgram.mutateAsync({
        id,
        data: value,
      });
    },
    [updateProgram],
  );

  const onDelete = React.useCallback(
    async (id: string) => {
      await deleteProgram.mutateAsync(id);
    },
    [deleteProgram],
  );

  const onAdd = React.useCallback(
    async (value: CreateProgramDTO) => {
      await createProgram.mutateAsync(value);
    },
    [createProgram],
  );

  return (
    <div className='min-h-3/4 w-full m-auto flex flex-row gap-4 p-4'>
      <GenericTable
        tableTitle={t('program:title')}
        queryHook={usePrograms}
        addAction={{
          title: t('program:addNew'),
          onAdd,
        }}
        columns={columns}
        actionCellProperties={{
          requireDeleteConfirmation: true,
          edit: {
            onSave,
          },
          delete: {
            onDelete,
          },
          detailComponent: ProgramDetail,
          formComponent: ProgramForm,
        }}
        filterOptions={[]}
      />
    </div>
  );
};

export default ProgramPage;
