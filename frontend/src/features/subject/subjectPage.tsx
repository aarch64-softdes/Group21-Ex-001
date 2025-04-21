import GenericTable from '@components/table/GenericTable';
import {
  useActivateSubject,
  useCreateSubject,
  useDeactivateSubject,
  useDeleteSubject,
  useSubjects,
  useUpdateSubject,
} from '@/features/subject/api/useSubjectApi';
import Subject, {
  CreateSubjectDTO,
  UpdateSubjectDTO,
} from '@/features/subject/types/subject';
import { Column } from '@/core/types/table';
import React, { useCallback } from 'react';
import SubjectForm from '@subject/components/SubjectForm';
import SubjectDetail from '@subject/components/SubjectDetail';

const SubjectPage: React.FC = () => {
  const createSubject = useCreateSubject();
  const updateSubject = useUpdateSubject();
  const deleteSubject = useDeleteSubject();
  const activateSubject = useActivateSubject();
  const deactivateSubject = useDeactivateSubject();

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
        key: 'faculty.name',
        nested: true,
      },
      {
        header: 'Status',
        key: 'isActive',
        style: {
          width: '100px',
        },
        transform: (value: boolean) => {
          return value ? 'Active' : 'Inactive';
        },
      },
    ],
    [],
  );

  const onSave = useCallback(
    async (id: string, value: UpdateSubjectDTO) => {
      await updateSubject.mutateAsync({
        id: id,
        data: value,
      });
    },
    [updateSubject],
  );

  const onDelete = useCallback(
    async (id: string) => {
      await deleteSubject.mutateAsync(id);
    },
    [deleteSubject],
  );

  const onAdd = useCallback(
    async (value: CreateSubjectDTO) => {
      await createSubject.mutateAsync(value);
    },
    [createSubject],
  );

  return (
    <div className='min-h-3/4 w-full m-auto flex flex-row gap-4 p-4'>
      <GenericTable
        tableTitle='Subject Management'
        addAction={{
          onAdd,
          title: 'Add Subject',
        }}
        queryHook={useSubjects}
        columns={columns}
        actionCellProperties={{
          requireDeleteConfirmation: true,
          edit: {
            onSave,
          },
          delete: {
            onDelete,
          },
          detailComponent: SubjectDetail,
          formComponent: SubjectForm,
          additionalActions: [
            {
              label: 'Activate',
              handler: async (id: string) => {
                await activateSubject.mutateAsync(id);
              },
              disabled: (row: Subject) => {
                return row.isActive;
              },
            },
            {
              label: 'Deactivate',
              handler: async (id: string) => {
                await deactivateSubject.mutateAsync(id);
              },
              disabled: (row: Subject) => {
                return !row.isActive;
              },
            },
          ],
        }}
        filterOptions={[]}
      />
    </div>
  );
};

export default SubjectPage;
