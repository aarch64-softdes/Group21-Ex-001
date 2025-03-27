import StatusDetail from '@/components/status/StatusDetail';
import StatusForm from '@/components/status/StatusForm';
import GenericTable from '@/components/table/GenericTable';
import {
  useCreateStatus,
  useDeleteStatus,
  useStatuses,
  useUpdateStatus,
} from '@/hooks/api/useStatusApi';
import Status, { CreateStatusDTO, UpdateStatusDTO } from '@/types/status';
import { Column } from '@/types/table';
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
      onSave: async (id: string, value: UpdateStatusDTO) => {
        await updateStatus.mutateAsync({
          id: parseInt(id, 10),
          data: value,
        });
      },
      onAdd: async (value: CreateStatusDTO) => {
        await createStatus.mutateAsync(value);
      },
      onDelete: async (id: string) => {
        await deleteStatus.mutateAsync(parseInt(id, 10));
      },
    }),
    [updateStatus, createStatus, deleteStatus],
  );

  return (
    <div className='min-h-3/4 w-full m-auto flex flex-row gap-4 p-4'>
      <GenericTable
        tableTitle='Status Management'
        addingTitle='Add Status'
        queryHook={useStatuses}
        columns={columns}
        actions={actions}
        formComponent={StatusForm}
        detailComponent={StatusDetail}
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

export default StatusPage;
