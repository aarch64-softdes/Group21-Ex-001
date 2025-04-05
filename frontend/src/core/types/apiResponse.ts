export interface ApiResponse<T> {
  data: T[];
  totalItems: number;
  totalPages: number;
  currentPage: number;
}
