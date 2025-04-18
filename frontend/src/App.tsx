import {
  QueryCache,
  QueryClient,
  QueryClientProvider,
} from '@tanstack/react-query';
import { Suspense } from 'react';
import {
  Navigate,
  Route,
  BrowserRouter as Router,
  Routes,
} from 'react-router-dom';

// Layout component to be created
import Layout from '@components/layouts/Layout';
import FacultyPage from '@faculty/facultyPage';
import ProgramPage from '@program/programPage';
import StatusPage from '@status/statusPage';
import StudentPage from '@student/studentPage';
import SettingPage from '@settings/settingPage';
import ErrorBoundary from '@components/error/ErrorBoundary';
import { ApiError } from '@lib/errors';
import { showErrorToast } from '@lib/toast-utils';
import SubjectPage from '@subject/subjectPage';
import CoursePage from '@/features/course/coursePage';
import StudentEnrollmentPage from './features/enrollment/pages/studentEnrollmentPage';
import CourseEnrollmentPage from './features/enrollment/pages/courseEnrollmentPage';
import AcademicTranscriptPage from './features/enrollment/pages/academicTranscriptPage';
import GradeManagementPage from './features/enrollment/pages/gradeManagmentPage';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: 1,
    },
    mutations: {},
  },
  queryCache: new QueryCache({
    onError: (error: ApiError) => {
      showErrorToast(error.message);
    },
  }),
});

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <Router>
        <ErrorBoundary>
          <Layout>
            <Suspense fallback={<div>Loading...</div>}>
              <Routes>
                <Route path='/' element={<Navigate to='/student' replace />} />
                <Route path='/student' element={<StudentPage />} />
                <Route path='/faculty' element={<FacultyPage />} />
                <Route path='/program' element={<ProgramPage />} />
                <Route path='/status' element={<StatusPage />} />
                <Route path='/subject' element={<SubjectPage />} />
                <Route path='/course' element={<CoursePage />} />
                <Route
                  path='/student/:studentId/enrollments'
                  element={<StudentEnrollmentPage />}
                />
                <Route
                  path='/course/:courseId/enrollments'
                  element={<CourseEnrollmentPage />}
                />
                <Route
                  path='/student/:studentId/transcript'
                  element={<AcademicTranscriptPage />}
                />
                <Route
                  path='/student/:studentId/grades'
                  element={<GradeManagementPage />}
                />
                <Route path='/setting' element={<SettingPage />} />
                <Route path='*' element={<Navigate to='/' replace />} />
              </Routes>
            </Suspense>
          </Layout>
        </ErrorBoundary>
      </Router>
    </QueryClientProvider>
  );
}

export default App;
