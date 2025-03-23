import { Component, ErrorInfo, ReactNode } from 'react';
import { PathNotFoundError, ResourceNotFoundError } from '@/lib/errors';
import ErrorPage from './ErrorPage';

interface Props {
  children: ReactNode;
}

interface State {
  hasError: boolean;
  error: Error | null;
}

class ErrorBoundary extends Component<Props, State> {
  public state: State = {
    hasError: false,
    error: null,
  };

  public static getDerivedStateFromError(error: Error): State {
    // Only capture specific errors we want to handle with our custom ErrorPage
    if (
      error instanceof ResourceNotFoundError ||
      error instanceof PathNotFoundError
    ) {
      return { hasError: true, error };
    }

    // For other errors, let them bubble up to the default React error handler
    throw error;
  }

  public componentDidCatch(error: Error, errorInfo: ErrorInfo) {
    console.error('Uncaught error:', error, errorInfo);
  }

  public render() {
    if (this.state.hasError && this.state.error) {
      return <ErrorPage error={this.state.error} />;
    }

    return this.props.children;
  }
}

export default ErrorBoundary;
