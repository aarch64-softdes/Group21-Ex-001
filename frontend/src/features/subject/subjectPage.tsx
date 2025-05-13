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
import { useTranslation } from 'react-i18next';

const SubjectPage: React.FC = () => {
  const { t } = useTranslation(['subject', 'common']);

  const createSubject = useCreateSubject();
  const updateSubject = useUpdateSubject();
  const deleteSubject = useDeleteSubject();
  const activateSubject = useActivateSubject();
  const deactivateSubject = useDeactivateSubject();

  const columns: Column<Subject>[] = React.useMemo(
    () => [
      {
        header: t('subject:fields.id'),
        key: 'id',
        style: {
          width: '80px',
        },
      },
      {
        header: t('subject:fields.code'),
        key: 'code',
        style: {
          width: '120px',
        },
      },
      {
        header: t('subject:fields.name'),
        key: 'name',
        isDefaultSort: true,
      },
      {
        header: t('subject:fields.credits'),
        key: 'credits',
        style: {
          width: '100px',
          textAlign: 'center',
        },
      },
      {
        header: t('subject:fields.faculty'),
        key: 'faculty.name',
        nested: true,
      },
      {
        header: t('subject:fields.isActive'),
        key: 'isActive',
        style: {
          width: '100px',
          textAlign: 'center',
        },
        transform: (value: boolean) => {
          return value
            ? t('subject:fields.status.active')
            : t('subject:fields.status.inactive');
        },
      },
    ],
    [t],
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
        tableTitle={t('subject:title')}
        addAction={{
          onAdd,
          title: t('subject:addNew'),
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
              label: t('subject:actions.activate'),
              handler: async (id: string) => {
                await activateSubject.mutateAsync(id);
              },
              disabled: (row: Subject) => {
                return row.isActive;
              },
            },
            {
              label: t('subject:actions.deactivate'),
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
