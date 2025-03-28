import { Button } from '@/components/ui/button';
import { AlertCircle, Home } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

interface ErrorPageProps {
  error: Error;
  status: number;
  title: string;
  resetError: () => void;
}

const ErrorPage: React.FC<ErrorPageProps> = ({
  error,
  status,
  title,
  resetError,
}) => {
  const navigate = useNavigate();

  return (
    <div className='h-screen w-full flex flex-col items-center justify-center bg-gray-50'>
      <div className='text-center max-w-md mx-auto p-6'>
        <div className='flex justify-center mb-4'>
          <AlertCircle className='h-24 w-24 text-red-500' />
        </div>

        <h1 className='text-4xl font-bold text-gray-900 mb-2'>{status}</h1>
        <h2 className='text-2xl font-semibold text-gray-800 mb-4'>{title}</h2>

        <p className='text-gray-600 mb-8'>{error.message}</p>

        <Button
          onClick={() => {
            navigate('/');
            resetError();
          }}
          className='flex items-center gap-2 w-full'
        >
          <Home className='h-4 w-4' />
          Return to Home
        </Button>
      </div>
    </div>
  );
};

export default ErrorPage;
