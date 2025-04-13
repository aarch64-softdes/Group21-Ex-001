import GenericTable from '@components/table/GenericTable';
import {
  useCreateCourse,
  useDeleteCourse,
  useCourses,
  useUpdateCourse,
} from '@/features/course/api/useCourseApi';
import Course, {
  CreateCourseDTO,
  UpdateCourseDTO,
} from '@/features/course/types/course';
import { Column } from '@/core/types/table';
import React from 'react';
import { SearchFilterOption } from '@/core/types/filter';
import { BookOpen, Users } from 'lucide-react';
import CourseForm from './components/CourseForm';
import CourseDetail from './components/CourseDetail';

const CoursePage: React.FC = () => {
  const createCourse = useCreateCourse();
  const updateCourse = useUpdateCourse();
  const deleteCourse = useDeleteCourse();

  const columns: Column<Course>[] = React.useMemo(
    () => [
      {
        header: 'Course Code',
        key: 'code',
        style: {
          width: '120px',
        },
      },
      {
        header: 'Subject',
        key: 'subject',
      },
      {
        header: 'Program',
        key: 'program',
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
          width: '60px',
        },
      },
      {
        header: 'Sem',
        key: 'semester',
        style: {
          width: '50px',
        },
      },
    ],
    [],
  );

  const actions = React.useMemo(
    () => ({
      onSave: async (id: string, value: UpdateCourseDTO) => {
        await updateCourse.mutateAsync({
          id,
          data: value,
        });
      },
      onAdd: async (value: CreateCourseDTO) => {
        await createCourse.mutateAsync(value);
      },
      onDelete: async (id: string) => {
        await deleteCourse.mutateAsync(id);
      },
    }),
    [updateCourse, createCourse, deleteCourse],
  );

  return (
    <div className='min-h-3/4 w-full m-auto flex flex-row gap-4 p-4'>
      <GenericTable
        tableTitle='Course Management'
        addingTitle='Add Course'
        queryHook={useCourses}
        columns={columns}
        actions={actions}
        formComponent={CourseForm}
        detailComponent={CourseDetail}
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

export default CoursePage;
