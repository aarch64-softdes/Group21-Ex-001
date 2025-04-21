import StatusDetail from '@status/components/StatusDetail';
import StatusForm from '@status/components/StatusForm';
import GenericTable from '@components/table/GenericTable';
import {
  useCreateStatus,
  useDeleteStatus,
  useStatuses,
  useUpdateStatus,
} from '@status/api/useStatusApi';
import Status, { CreateStatusDTO, UpdateStatusDTO } from '@status/types/status';
import { Column } from '@/core/types/table';
import React from 'react';

const StatusPage: React.FC = () => {
  const createStatus = useCreateStatus();
  const updateStatus = useUpdateStatus();
  const deleteStatus = useDeleteStatus();

  const columns: Column<Status>[] = React.useMemo(
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
      onAdd: async (value: CreateStatusDTO) => {
        await createStatus.mutateAsync(value);
      },
    }),
    [createStatus],
  );

  const onSave = React.useCallback(
    async (id: string, value: UpdateStatusDTO) => {
      await updateStatus.mutateAsync({
        id,
        data: value,
      });
    },
    [updateStatus],
  );

  const onDelete = React.useCallback(
    async (id: string) => {
      await deleteStatus.mutateAsync(id);
    },
    [deleteStatus],
  );

  return (
    <div className='min-h-3/4 w-full m-auto flex flex-row gap-4 p-4'>
      <GenericTable
        tableTitle='Status Management'
        addingTitle='Add Status'
        queryHook={useStatuses}
        columns={columns}
        actions={actions}
        actionCellProperties={{
          requireDeleteConfirmation: true,
          edit: {
            onSave,
          },
          delete: {
            onDelete,
          },
          detailComponent: StatusDetail,
          formComponent: StatusForm,
        }}
        filterOptions={[]}
      />
    </div>
  );
};

export default StatusPage;
