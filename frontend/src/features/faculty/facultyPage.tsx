import GenericTable from '@components/table/GenericTable';
import FacultyForm from '@/features/faculty/components/FacultyForm';
import FacultyDetail from '@/features/faculty/components/FacultyDetail';
import React from 'react';
import { useTranslation } from 'react-i18next';

import {
  useCreateFaculty,
  useDeleteFaculty,
  useFaculties,
  useUpdateFaculty,
} from '@/features/faculty/api/useFacultyApi';

import { Column } from '@/core/types/table';
import Faculty, {
  CreateFacultyDTO,
  UpdateFacultyDTO,
} from '@/features/faculty/types/faculty';

const FacultyPage: React.FC = () => {
  const { t } = useTranslation(['faculty', 'common']);

  const createFaculty = useCreateFaculty();
  const updateFaculty = useUpdateFaculty();
  const deleteFaculty = useDeleteFaculty();

  const columns: Column<Faculty>[] = React.useMemo(
    () => [
      {
        header: t('faculty:fields.id'),
        key: 'id',
        style: {
          width: '80px',
        },
      },
      {
        header: t('faculty:fields.name'),
        key: 'name',
      },
    ],
    [t],
  );

  const actions = React.useMemo(
    () => ({
      onSave: async (id: string, value: UpdateFacultyDTO) => {
        await updateFaculty.mutateAsync({
          id: id,
          data: value,
        });
      },
      onAdd: async (value: CreateFacultyDTO) => {
        await createFaculty.mutateAsync(value);
      },
      onDelete: async (id: string) => {
        await deleteFaculty.mutateAsync(id);
      },
    }),
    [updateFaculty, createFaculty, deleteFaculty],
  );

  return (
    <div className='min-h-3/4 w-full m-auto flex flex-row gap-4 p-4'>
      <GenericTable
        tableTitle={t('faculty:title')}
        addingTitle={t('faculty:addNew')}
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
      />
    </div>
  );
};

export default FacultyPage;
