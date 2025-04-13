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

interface ExtendedCourse extends Course {
  subjectName?: string;
  programName?: string;
}

const CoursePage: React.FC = () => {
  const createCourse = useCreateCourse();
  const updateCourse = useUpdateCourse();
  const deleteCourse = useDeleteCourse();

  const columns: Column<ExtendedCourse>[] = React.useMemo(
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
        filterOptions={[
          searchCodeFilterOption,
          searchLecturerFilterOption,
          searchSubjectFilterOption,
        ]}
      />
    </div>
  );
};

export default CoursePage;
