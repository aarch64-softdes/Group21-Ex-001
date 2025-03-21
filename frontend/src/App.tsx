// App.tsx
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { Suspense } from 'react';
import {
  Navigate,
  Route,
  BrowserRouter as Router,
  Routes,
} from 'react-router-dom';

// Layout component to be created
import Layout from './components/layouts/Layout';
import FacultyPage from './pages/facultyPage';
import ProgramPage from './pages/programPage';
import StatusPage from './pages/statusPage';
import StudentPage from './pages/studentPage';

const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <Router>
        <Layout>
          <Suspense fallback={<div>Loading...</div>}>
            <Routes>
              <Route path='/' element={<Navigate to='/student' replace />} />
              <Route path='/student' element={<StudentPage />} />
              <Route path='/faculty' element={<FacultyPage />} />
              <Route path='/program' element={<ProgramPage />} />
              <Route path='/status' element={<StatusPage />} />
              <Route path='*' element={<Navigate to='/' replace />} />
            </Routes>
          </Suspense>
        </Layout>
      </Router>
    </QueryClientProvider>
  );
}

export default App;
