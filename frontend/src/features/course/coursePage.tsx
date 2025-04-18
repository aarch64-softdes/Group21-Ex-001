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
import CourseForm from './components/CourseForm';
import CourseDetail from './components/CourseDetail';
import { useNavigate } from 'react-router-dom';

const CoursePage: React.FC = () => {
  const navigate = useNavigate();

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
        key: 'subject.name',
        nested: true,
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
        header: 'Year',
        key: 'year',
        style: {
          width: '60px',
        },
      },
      {
        header: 'Semester',
        key: 'semester',
        style: {
          width: '80px',
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
        additionalActions={[
          {
            label: 'Enrollment',
            handler(id) {
              navigate(`/course/${id}/enrollments`);
            },
          },
        ]}
      />
    </div>
  );
};

export default CoursePage;
