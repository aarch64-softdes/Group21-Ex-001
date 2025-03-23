export class AppError extends Error {
  constructor(message: string) {
    super(message);
    this.name = 'AppError';
  }
}

export class ResourceNotFoundError extends AppError {
  constructor(resource: string, id?: string) {
    super(`${resource}${id ? ` with ID ${id}` : ''} not found`);
    this.name = 'ResourceNotFoundError';
  }
}

export class PathNotFoundError extends AppError {
  constructor(path?: string) {
    super(`Path${path ? ` "${path}"` : ''} not found`);
    this.name = 'PathNotFoundError';
  }
}

// Helper to extract error message from various error types
export function getErrorMessage(error: unknown): string {
  if (error instanceof Error) {
    return error.message;
  }

  if (typeof error === 'string') {
    return error;
  }

  return 'An unknown error occurred';
}
