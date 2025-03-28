import { Component, ErrorInfo, ReactNode } from 'react';
import ErrorPage from './ErrorPage';

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
    return {
      hasError: true,
      error,
      status: 500,
      title: 'An error occurred',
    };
  }

  public componentDidCatch(error: Error, errorInfo: ErrorInfo) {
    console.error('ErrorBoundary caught an error:', error, errorInfo);
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
