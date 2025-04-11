import GenericTable from '@components/table/GenericTable';
import {
  useCreateClass,
  useDeleteClass,
  useClasses,
  useUpdateClass,
} from '@/features/class/api/useClassApi';
import Class, {
  CreateClassDto,
  UpdateClassDto,
} from '@/features/class/types/class';
import { Column } from '@/core/types/table';
import React from 'react';
import { SearchFilterOption } from '@/core/types/filter';
import { BookOpen, Users } from 'lucide-react';
import ClassForm from './components/ClassForm';
import ClassDetail from './components/ClassDetail';

interface ExtendedClass extends Class {
  subjectName?: string;
  programName?: string;
}

const ClassPage: React.FC = () => {
  const createClass = useCreateClass();
  const updateClass = useUpdateClass();
  const deleteClass = useDeleteClass();

  const columns: Column<ExtendedClass>[] = React.useMemo(
    () => [
      {
        header: 'Class Code',
        key: 'code',
        style: {
          width: '120px',
        },
      },
      {
        header: 'Subject',
        key: 'subject.name',
        nested: true,
      },
      {
        header: 'Program',
        key: 'program.name',
        nested: true,
      },
      {
        header: 'Lecturer',
        key: 'lecturer',
      },
      {
        header: 'Room',
        key: 'room',
        style: {
          width: '80px',
        },
      },
      {
        header: 'Schedule',
        key: 'schedule',
        style: {
          width: '100px',
        },
      },
      {
        header: 'Max Students',
        key: 'maxStudent',
        style: {
          width: '80px',
        },
      },
      {
        header: 'Year',
        key: 'year',
        style: {
          width: '80px',
        },
      },
    ],
    [],
  );

  const actions = React.useMemo(
    () => ({
      onSave: async (id: string, value: UpdateClassDto) => {
        await updateClass.mutateAsync({
          id,
          data: value,
        });
      },
      onAdd: async (value: CreateClassDto) => {
        await createClass.mutateAsync(value);
      },
      onDelete: async (id: string) => {
        await deleteClass.mutateAsync(id);
      },
    }),
    [updateClass, createClass, deleteClass],
  );

  const searchCodeFilterOption: SearchFilterOption = {
    id: 'search',
    label: 'Search by code or room',
    labelIcon: BookOpen,
    placeholder: 'Enter code or room',
    type: 'search',
  };

  const searchLecturerFilterOption: SearchFilterOption = {
    id: 'lecturer',
    label: 'Search by lecturer',
    labelIcon: Users,
    placeholder: 'Enter lecturer name',
    type: 'search',
  };

  const searchSubjectFilterOption: SearchFilterOption = {
    id: 'subject',
    label: 'Search by subject',
    labelIcon: BookOpen,
    placeholder: 'Enter subject name',
    type: 'search',
  };

  return (
    <div className='min-h-3/4 w-full m-auto flex flex-row gap-4 p-4'>
      <GenericTable
        tableTitle='Class Management'
        addingTitle='Add Class'
        queryHook={useClasses}
        columns={columns}
        actions={actions}
        formComponent={ClassForm}
        detailComponent={ClassDetail}
        disabledActions={{
          edit: false,
          delete: false,
        }}
        requireDeleteConfirmation={true}
        filterOptions={[
          searchCodeFilterOption,
          searchLecturerFilterOption,
          searchSubjectFilterOption,
        ]}
      />
    </div>
  );
};

export default ClassPage;
