import { Column } from '@/core/types/table';
import {
  useCourses,
  useCreateCourse,
  useDeleteCourse,
  useUpdateCourse,
} from '@/features/course/api/useCourseApi';
import Course, {
  CreateCourseDTO,
  UpdateCourseDTO,
} from '@/features/course/types/course';
import GenericTable from '@components/table/GenericTable';
import React from 'react';
import { useNavigate } from 'react-router-dom';
import CourseDetail from './components/CourseDetail';
import CourseForm from './components/CourseForm';
import { useTranslation } from 'react-i18next';

const CoursePage: React.FC = () => {
  const navigate = useNavigate();
  const { t } = useTranslation(['course', 'common']);

  const createCourse = useCreateCourse();
  const updateCourse = useUpdateCourse();
  const deleteCourse = useDeleteCourse();

  const columns: Column<Course>[] = React.useMemo(
    () => [
      {
        header: t('course:fields.code'),
        key: 'code',
        style: {
          width: '120px',
        },
      },
      {
        header: t('course:fields.subject'),
        key: 'subject.name',
        nested: true,
      },
      {
        header: t('course:fields.room'),
        key: 'room',
        style: {
          width: '80px',
          textAlign: 'center',
        },
      },
      {
        header: t('course:fields.schedule'),
        key: 'schedule',
        style: {
          width: '100px',
          textAlign: 'center',
        },
      },
      {
        header: t('course:fields.year'),
        key: 'year',
        style: {
          width: '60px',
          textAlign: 'center',
        },
      },
      {
        header: t('course:fields.semester'),
        key: 'semester',
        style: {
          width: '80px',
          textAlign: 'center',
        },
      },
    ],
    [t],
  );

  const onSave = React.useCallback(
    async (id: string, value: UpdateCourseDTO) => {
      await updateCourse.mutateAsync({
        id,
        data: value,
      });
    },
    [updateCourse],
  );

  const onDelete = React.useCallback(
    async (id: string) => {
      await deleteCourse.mutateAsync(id);
    },
    [deleteCourse],
  );

  const onAdd = React.useCallback(
    async (value: CreateCourseDTO) => {
      await createCourse.mutateAsync(value);
    },
    [createCourse],
  );

  return (
    <div className='min-h-3/4 w-full m-auto flex flex-row gap-4 p-4'>
      <GenericTable
        tableTitle={t('course:title')}
        addAction={{
          onAdd,
          disabled: false,
          title: t('course:addNew'),
        }}
        queryHook={useCourses}
        columns={columns}
        actionCellProperties={{
          requireDeleteConfirmation: true,
          edit: {
            onSave,
            disabled: false,
          },
          delete: {
            onDelete,
            disabled: false,
          },
          detailComponent: CourseDetail,
          formComponent: CourseForm,
          additionalActions: [
            {
              label: t('enrollment:title'),
              handler(id) {
                navigate(`/course/${id}/enrollments`);
              },
            },
          ],
        }}
        filterOptions={[]}
      />
    </div>
  );
};

export default CoursePage;
