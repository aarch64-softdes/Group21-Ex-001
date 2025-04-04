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
import Layout from './components/layouts/Layout';
import FacultyPage from './features/faculty/facultyPage';
import ProgramPage from './features/program/programPage';
import StatusPage from './features/status/statusPage';
import StudentPage from './features/student/studentPage';
import ErrorBoundary from './components/error/ErrorBoundary';
import { ApiError } from './shared/lib/errors';
import { showErrorToast } from './shared/lib/toast-utils';
import SettingPage from './features/settings/settingPage';

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
