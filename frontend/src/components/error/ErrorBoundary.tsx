import { Component, ErrorInfo, ReactNode } from 'react';
import { ResourceNotFoundError } from '@/lib/errors';
import ErrorPage from './ErrorPage';
import { toast } from 'sonner';
import { showErrorToast } from '@/lib/toast-utils';

interface Props {
  children: ReactNode;
}

interface State {
  hasError: boolean;
  error: Error | null;
  status: number;
  title: string;
}

class ErrorBoundary extends Component<Props, State> {
  public state: State = {
    hasError: false,
    error: null,
    status: 0,
    title: '',
  };

  public static getDerivedStateFromError(error: Error): State {
    // Only show ErrorPage for ResourceNotFoundError
    if (error instanceof ResourceNotFoundError) {
      return {
        hasError: true,
        error,
        status: 404,
        title: 'Resource Not Found',
      };
    }

    throw error;
  }

  public componentDidCatch(error: Error, errorInfo: ErrorInfo) {
    showErrorToast(error.message || 'An unexpected error occurred');
  }

  public resetError = () => {
    this.setState({
      hasError: false,
      error: null,
      status: 0,
      title: '',
    });
  };

  public render() {
    if (this.state.hasError && this.state.error) {
      return (
        <ErrorPage
          error={this.state.error}
          status={this.state.status}
          title={this.state.title}
          resetError={this.resetError}
        />
      );
    }

    return this.props.children;
  }
}

export default ErrorBoundary;
